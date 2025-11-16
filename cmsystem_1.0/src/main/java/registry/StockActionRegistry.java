package registry;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;

import dtos.StockDTO;
import interfaces.ActionHandler;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import model.Sale;
import service.PurchaseService;
import service.StockService;
import servlets.StockServlet;
import utils.Const;
import utils.HandleForward;
import utils.HandleStrings;
import utils.HandleTimeData;
import utils.PtBr;

public class StockActionRegistry {
	
	private static final Map<String, ActionHandler> registry = new HashMap<>();

	static {

		registry.put("supplyList", new GetListOfSupplierStock());
		registry.put("sell", new GetDataToSell());
		registry.put("stock", new GetStockInfoAjax());
		registry.put("updateStock", new UpdateStock());
		registry.put("detail", new GetStockInfoToPurchase());
		registry.put("ownerStock", new GetInfoFromStockOwner());
		registry.put("searchByName", new SearchStockInfoByNameLetters());
		registry.put("searchProduct", new SearchStockInfoByProduct());
		registry.put(null, new GetStockList());
		
	}

	public static ActionHandler get(String action) {
		return registry.get(action);
	}

}

class GetListOfSupplierStock implements ActionHandler {

	private StockService service = new StockService();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, Logger logger)
			throws ServletException, IOException {
		
		
		HashMap<String, Object> map = new HashMap<>();
		List<StockDTO> stockList = null;
		Map<String, Object> totalStock = null;

		try {
			String productID = (String) request.getParameter("productID");
			
			if(productID == null) {
				HandleForward.errorForward(request, response, "Erro ao capturar o Id do produto",
						"erro.jsp");
			}
			
			Long prodID = Long.parseLong(productID);

			stockList = service.getSupplyStock(prodID);

			totalStock = TotalStock.get();

			map.put("count", stockList.size());
			map.put("tableData", stockList);
			map.put("cafe", totalStock.get("cafe"));
			map.put("pimenta", totalStock.get("pimenta"));
			map.put("stockUpdateDate", HandleTimeData.getActualFormattedDateTime());

			request.setAttribute("dataStock", map);
			RequestDispatcher rd = request.getRequestDispatcher(Const.ROOT_PAGE_PATH + "/stock.jsp");
			rd.forward(request, response);

		} catch (Exception e) {
			logger.error("Erro ao tentar buscar os dados para a Stock.jsp", e);
			HandleForward.errorForward(request, response, "Erro ao tentar buscar os dados para a Stock.jsp",
					"erro.jsp");
		}
		
	}
	
}

class GetDataToSell implements ActionHandler {
	
	private StockService service = new StockService();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, Logger logger)
			throws ServletException, IOException {
		
		String productID = (String) request.getParameter("productID");

		if (productID == null) {
			HandleForward.errorForward(request, response, "Os parâmetros não podem ser nullos", "error.jsp");
			return;
		}

		try {
			StockDTO ownerStock = this.service.getOwnerStock(productID);

			if (ownerStock == null) {
				HandleForward.forward(request, response, "Crie seu estoque pessoal primeiro", "transactions.jsp");
				return;
			}

			Double resultStock = ownerStock.getQuantity();

			request.setAttribute("productID", productID);
			request.setAttribute("clientID", Const.OWNER_ID);
			request.setAttribute("client", ownerStock.getName());
			request.setAttribute("product", ownerStock.getProduct());
			request.setAttribute("quantity", PtBr.formatDecimal(resultStock));

			HandleForward.forward(request, response, "", "sale.jsp");

		} catch (Exception e) {
			HandleForward.errorForward(request, response, "Erro ao buscar dados do estoque", "erro.jsp");

		}
		
	}
	
}

class GetStockInfoAjax implements ActionHandler {
	private StockService service = new StockService();
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, Logger logger)
			throws ServletException, IOException {
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		Map<String, Object> data = null;

		try {
			data = service.getTotalStock();
			logger.info("Ajax para estoque bem sucedida.");

		} catch (Exception e) {
			logger.error("Erro na busca com AJAX. {}", e);
		}

		data.put("stockUpdateDate", HandleTimeData.getActualFormattedDateTime());

		String json = new Gson().toJson(data);
		response.getWriter().write(json);
		
	}
	
}

class UpdateStock implements ActionHandler {
	private StockService service = new StockService();
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, Logger logger)
			throws ServletException, IOException {
		
		String stockId = request.getParameter("stockId");
		String quantitySoldStr = request.getParameter("quantitySold");
		Double quantitySold = Double.parseDouble(quantitySoldStr);

		HashMap<String, Object> map = new HashMap<>();
		List<StockDTO> stockList = null;
		Map<String, Object> totalStock = null;

		try {
			stockList = service.updateStockAndReturnStockList(stockId, quantitySold);

			if (stockList != null && !stockList.isEmpty()) {

				totalStock = TotalStock.get();

				map.put("tableData", stockList);
				map.put("cafe", totalStock.get("cafe"));
				map.put("pimenta", totalStock.get("pimenta"));
				map.put("stockUpdateDate", HandleTimeData.getActualFormattedDateTime());

				request.getSession().setAttribute("dataStock", map);
				HandleForward.forward(request, response, "", "stock.jsp");

			} else {
				logger.error("Erro ao buscar a lista atualizada após update do estoque");
			}

		} catch (Exception e) {
			logger.error("Erro ao tentar buscar os dados para a Stock.jsp");
			HandleForward.errorForward(request, response, "Erro ao buscar os dados da página", "erro.jsp");
		}
		
	}
	
}

class GetStockInfoToPurchase implements ActionHandler {
	private StockService service = new StockService();
	private PurchaseService purchaseService = new PurchaseService();
	
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, Logger logger)
			throws ServletException, IOException {
		
		try {
			
			String stockId = request.getParameter("stockId");
			String clientID = request.getParameter("clientID");
			StockDTO stock = service.getStockById(stockId);
			
			Long productID = "pimenta".equalsIgnoreCase(stock.getProduct()) ? 2L : 1L;
			Sale saleActive = this.purchaseService.getActiveSaleByProductID(productID);
			
			if(saleActive != null) {
				
				request.setAttribute("saleActive", saleActive);
				String fmtPendency = PtBr.formatDecimal(saleActive.getPendency());
				request.setAttribute("fmtPendency", fmtPendency);
			}

			request.setAttribute("stockId", stockId);
			request.setAttribute("clientName", stock.getName());
			request.setAttribute("clientID", clientID);
			request.setAttribute("clientProduct", stock.getProduct());
			request.setAttribute("amount", stock.getQuantity());
			request.setAttribute("cpf", stock.getCpf());
			request.setAttribute("unit", stock.getUnit());
			RequestDispatcher rd = request.getRequestDispatcher(Const.ROOT_PAGE_PATH + "/receipt-stock-purchase.jsp");
			rd.forward(request, response);

		} catch (Exception e) {
			logger.error("Falha ao buscar os detalhes do estoque", e);
			HandleForward.errorForward(request, response, "Estoque não encontrado", "erro.jsp");
		}
		
	}
	
}

class GetInfoFromStockOwner implements ActionHandler {
	private StockService service = new StockService();
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, Logger logger)
			throws ServletException, IOException {
		
		HashMap<String, Object> map = new HashMap<>();
		List<StockDTO> stockList = null;
		Map<String, Object> totalStock = null;
		
		try {
			
			stockList = service.getTableDataByOwner();
			totalStock = TotalStock.get();
			
			map.put("count", stockList.size());
			map.put("tableData", stockList);
			map.put("cafe", totalStock.get("cafe"));
			map.put("pimenta", totalStock.get("pimenta"));
			map.put("stockUpdateDate", HandleTimeData.getActualFormattedDateTime());
			
			request.setAttribute("dataStock", map);
			RequestDispatcher rd = request.getRequestDispatcher(Const.ROOT_PAGE_PATH + "/stock.jsp");
			rd.forward(request, response);
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
}

class SearchStockInfoByNameLetters implements ActionHandler {
	private StockService service = new StockService();
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, Logger logger)
			throws ServletException, IOException {
		
		String search = (String) request.getParameter("search");
		
		if (search == null) {
			HandleForward.forward(request, response, "O termo de busca precisa ser válido", "stock.jsp" );
		}
		
		HashMap<String, Object> map = new HashMap<>();
		List<StockDTO> stockList = null;
		Map<String, Object> totalStock = null;
		
		try {
			
			stockList = service.getTableDataByName(search);
			totalStock = TotalStock.get();
			
			map.put("count", stockList.size());
			map.put("tableData", stockList);
			map.put("cafe", totalStock.get("cafe"));
			map.put("pimenta", totalStock.get("pimenta"));
			map.put("stockUpdateDate", HandleTimeData.getActualFormattedDateTime());
			
			request.setAttribute("dataStock", map);
			RequestDispatcher rd = request.getRequestDispatcher(Const.ROOT_PAGE_PATH + "/stock.jsp");
			rd.forward(request, response);
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
}
class SearchStockInfoByProduct implements ActionHandler {
	private StockService service = new StockService();
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, Logger logger)
			throws ServletException, IOException {
		
		String search = (String) request.getParameter("search");
		String withPositiveStock = request.getParameter("withPositiveStock");
		
		if (search == null) {
			HandleForward.forward(request, response, "O termo de busca precisa ser válido", "stock.jsp" );
		}
		
		Boolean onlyPositiveStock = withPositiveStock != null ? true : false;
		
		HashMap<String, Object> map = new HashMap<>();
		List<StockDTO> stockList = null;
		Map<String, Object> totalStock = null;
		
		try {
			
			stockList = service.getTableDataByProduct(search, onlyPositiveStock);
			totalStock = TotalStock.get();

			map.put("count", stockList.size());
			map.put("tableData", stockList);
			map.put("cafe", totalStock.get("cafe"));
			map.put("pimenta", totalStock.get("pimenta"));
			map.put("stockUpdateDate", HandleTimeData.getActualFormattedDateTime());

			request.setAttribute("dataStock", map);
			RequestDispatcher rd = request.getRequestDispatcher(Const.ROOT_PAGE_PATH + "/stock.jsp");
			rd.forward(request, response);

			
		} catch (Exception e) {
			// TODO: handle exception
		}
				
	}
	
}

class GetStockList implements ActionHandler {
	private StockService service = new StockService();
	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response, Logger logger)
			throws ServletException, IOException {
		
		HashMap<String, Object> map = new HashMap<>();
		List<StockDTO> stockList = null;
		Map<String, Object> totalStock = null;
		
		try {
			
			stockList = service.getStock();
			totalStock = TotalStock.get();
			
			map.put("count", stockList.size());
			map.put("tableData", stockList);
			map.put("cafe", totalStock.get("cafe"));
			map.put("pimenta", totalStock.get("pimenta"));
			map.put("stockUpdateDate", HandleTimeData.getActualFormattedDateTime());
			
			request.setAttribute("dataStock", map);
			RequestDispatcher rd = request.getRequestDispatcher(Const.ROOT_PAGE_PATH + "/stock.jsp");
			rd.forward(request, response);
			
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
	
}

class TotalStock {
	private static StockService service = new StockService();
	final static Logger logger = LoggerFactory.getLogger(StockServlet.class);
	
	public static  Map<String, Object> get() {
		Map<String, Object> result = null;

		try {
			result = service.getTotalStock();
					
		} catch (Exception e) {
			logger.error("Erro ao buscar o estoque total. {}", e);
		}

		return result;
	}
}


//end file