package daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import connection.ConnectionFactory;
import dtos.StockDTO;
import exceptions.AtomicStockQueryException;
import model.Entry;
import model.Stock;
import utils.Const;
import utils.PtBr;
import utils.UtilsDao;

public class StockDAO {

	final Logger logger = LoggerFactory.getLogger(StockDAO.class);

	public boolean saveStock(Stock stock) throws Exception {
		Connection connection = null;
		PreparedStatement stmt = null;
		boolean saved = false;

		try {
			String sql = "INSERT INTO cm_system.stock (client_id, product_id, quantity)" + " VALUES (?, ?, ?)";

			connection = ConnectionFactory.conectar();

			stmt = connection.prepareStatement(sql);
			stmt.setLong(1, stock.getClientId());
			stmt.setLong(2, stock.getProductId());
			stmt.setDouble(3, stock.getQuantity());

			int rowsAffected = stmt.executeUpdate();
			saved = rowsAffected > 0;

		} catch (Exception e) {
			logger.error("saveStock() -> Erro ao salvar o estoque", e);
			throw e;
		} finally {
			UtilsDao.closeDAOResources(null, stmt, connection, logger);
		}

		return saved;
	}

	public boolean updateClientStock(Stock stock) throws Exception {
		Connection connection = null;
		PreparedStatement stmt = null;
		boolean updated = false;

		try {
			String sql = "UPDATE cm_system.stock SET quantity = ?" + " WHERE client_id = ? AND product_id = ?";

			connection = ConnectionFactory.conectar();

			stmt = connection.prepareStatement(sql);
			stmt.setDouble(1, stock.getQuantity());
			stmt.setLong(2, stock.getClientId());
			stmt.setLong(3, stock.getProductId());

			int rowsAffected = stmt.executeUpdate();
			updated = rowsAffected > 0;

		} catch (Exception e) {
			logger.error("updateClientStock() -> Erro ao atualizar o estoque do cliente", e);
			throw e;
		} finally {
			UtilsDao.closeDAOResources(null, stmt, connection, logger);
		}

		return updated;
	}

	public Stock getStock(Entry entry) throws Exception {

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Stock resultStock = null;

		try {
			String sql = "SELECT id, client_id, product_id, quantity FROM cm_system.stock"
					+ "	 WHERE client_id = ? and product_id = ?";

			connection = ConnectionFactory.conectar();

			stmt = connection.prepareStatement(sql);
			stmt.setLong(1, entry.getClientId());
			stmt.setLong(2, entry.getProduct_id());
			rs = stmt.executeQuery();

			if (!rs.next()) {
				return null;
			}

			resultStock = new Stock();
			resultStock.setId(rs.getLong("id"));
			resultStock.setClientId(rs.getLong("client_id"));
			resultStock.setProductId(rs.getLong("product_id"));
			resultStock.setQuantity(rs.getDouble("quantity"));

		} catch (Exception e) {
			logger.error("getStock() -> Erro ao buscar o estoque por cliente e produtos IDs", e);
			throw e;
		} finally {
			UtilsDao.closeDAOResources(rs, stmt, connection, logger);
		}

		return resultStock;
	}

	public StockDTO getStockByClientAndProduct(Long clienteID, Long prodductID) throws Exception {

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StockDTO resultStock = null;

		try {
			String sql = "SELECT  c.name AS name, p.name AS product, p.unit AS unit,"
					+ " st.quantity AS quantity, st.id" 
					+ " FROM cm_system.stock AS st"
					+ " INNER JOIN  clients c ON st.client_id = c.id" + " INNER JOIN products p ON st.product_id = p.id"
					+ " WHERE  st.client_id = ? AND st.product_id = ?";

			connection = ConnectionFactory.conectar();

			stmt = connection.prepareStatement(sql);
			stmt.setLong(1, clienteID);
			stmt.setLong(2, prodductID);
			rs = stmt.executeQuery();

			if (!rs.next()) {
				return null;
			}

			resultStock = new StockDTO();
			resultStock.setId(rs.getLong("id"));
			resultStock.setName(rs.getString("name"));
			resultStock.setProduct(rs.getString("product"));
			resultStock.setNumQuantity(rs.getDouble("quantity"));

		} catch (Exception e) {
			logger.error("getStockByClientAndProduct() -> Erro ao buscar o estoque por cliente", e);
			throw e;
		} finally {
			UtilsDao.closeDAOResources(rs, stmt, connection, logger);
		}

		return resultStock;
	}

	public Stock getActualStock(Stock stock) throws Exception {

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		Stock resultStock = null;

		try {
			String sql = "SELECT id, client_id, product_id, quantity" 
					+ " FROM cm_system.stock"
					+ " WHERE client_id = ? AND product_id = ?";

			connection = ConnectionFactory.conectar();

			stmt = connection.prepareStatement(sql);
			stmt.setLong(1, stock.getClientId());
			stmt.setLong(2, stock.getProductId());
			rs = stmt.executeQuery();

			if (!rs.next()) {
				return resultStock;
			}

			resultStock = new Stock();
			resultStock.setId(rs.getLong("id"));// pegando o id que não vinha antes
			resultStock.setClientId(rs.getLong("client_id"));
			resultStock.setProductId(rs.getLong("product_id"));
			resultStock.setQuantity(rs.getDouble("quantity"));

		} catch (Exception e) {
			logger.error("getActualStock() -> Erro ao buscar o estoque por cliente", e);
			throw e;
		} finally {
			UtilsDao.closeDAOResources(rs, stmt, connection, logger);
		}

		return resultStock;
	}

	public List<StockDTO> getAllStocks() throws Exception {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<StockDTO> stockList = new ArrayList<>();

		try {

			String sql = "SELECT  c.name AS name, c.id AS client_id, p.name AS product, p.unit AS unit,"
					+ " st.quantity AS quantity, st.id" 
					+ " FROM cm_system.stock AS st"
					+ " INNER JOIN  clients c ON st.client_id = c.id"
					+ " INNER JOIN products p ON st.product_id = p.id;";

			connection = ConnectionFactory.conectar();

			stmt = connection.prepareStatement(sql);
			rs = stmt.executeQuery();

			while (rs.next()) {
				StockDTO stock = new StockDTO();
				stock.setId(rs.getLong("id"));
				stock.setName(rs.getString("name"));
				stock.setClientID(rs.getLong("client_id"));
				stock.setProduct(rs.getString("product"));
				stock.setNumQuantity(rs.getDouble("quantity"));
				stock.setUnitByProduct(rs.getString("product"));

				stockList.add(stock);
			}

		} catch (Exception e) {
			logger.error("getAllStocks() -> Erro ao buscar todos os estoques", e);
			throw e;
		} finally {
			UtilsDao.closeDAOResources(rs, stmt, connection, logger);
		}

		return stockList;
	}

	public List<StockDTO> getAllStocksByProduct(String product, Boolean onlyPositiveStock) throws Exception {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<StockDTO> stockList = new ArrayList<>();
		String sql = "";

		try {
			connection = ConnectionFactory.conectar();

			if (onlyPositiveStock) {

				sql = "SELECT  c.name AS name, p.name AS product, p.unit AS unit,"
						+ " st.quantity AS quantity, st.id, st.client_id"
						+ " FROM cm_system.stock AS st INNER JOIN  clients c ON st.client_id = c.id"
						+ " INNER JOIN products p ON st.product_id = p.id"
						+ " WHERE  p.name = ? AND st.quantity > 0 AND st.client_id != ?";
				stmt = connection.prepareStatement(sql);
				stmt.setString(1, product);
				stmt.setLong(2, Const.OWNER_ID);
				rs = stmt.executeQuery();

			} else {

				sql = "SELECT  c.name AS name, p.name AS product, p.unit AS unit,"
						+ " st.quantity AS quantity, st.id, st.client_id"
						+ " FROM cm_system.stock AS st INNER JOIN  clients c ON st.client_id = c.id"
						+ " INNER JOIN products p ON st.product_id = p.id" + " WHERE  p.name = ?";
				stmt = connection.prepareStatement(sql);
				stmt.setString(1, product);
				rs = stmt.executeQuery();
			}

			while (rs.next()) {
				StockDTO stock = new StockDTO();
				stock.setId(rs.getLong("id"));
				stock.setName(rs.getString("name"));
				stock.setProduct(rs.getString("product"));
				stock.setClientID(rs.getLong("client_id"));
				stock.setNumQuantity(rs.getDouble("quantity"));
				stock.setUnitByProduct(rs.getString("product"));

				stockList.add(stock);
			}

		} catch (Exception e) {
			logger.error("getAllStocksByProduct() -> Erro ao buscar todos os estoques", e);
			throw e;
		} finally {
			UtilsDao.closeDAOResources(rs, stmt, connection, logger);
		}

		return stockList;
	}

	public List<StockDTO> getAllStocksByClientName(String clientName) throws Exception {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<StockDTO> stockList = new ArrayList<>();

		try {

			String sql = "SELECT  c.name AS name, p.name AS product, p.unit AS unit,"
					+ " st.quantity AS quantity, st.id, st.client_id" 
					+ " FROM cm_system.stock AS st"
					+ " INNER JOIN  clients c ON st.client_id = c.id" + " INNER JOIN products p ON st.product_id = p.id"
					+ " WHERE UPPER(c.name) like UPPER(?)";

			connection = ConnectionFactory.conectar();

			stmt = connection.prepareStatement(sql);
			stmt.setString(1, "%" + clientName + "%");
			rs = stmt.executeQuery();

			while (rs.next()) {
				StockDTO stock = new StockDTO();
				stock.setId(rs.getLong("id"));
				stock.setName(rs.getString("name"));
				stock.setProduct(rs.getString("product"));
				stock.setNumQuantity(rs.getDouble("quantity"));
				stock.setUnitByProduct(rs.getString("product"));
				stock.setClientID(rs.getLong("client_id"));

				stockList.add(stock);
			}

		} catch (Exception e) {
			logger.error("getAllStocksByClientName() -> Erro ao buscar todos os estoques", e);
			throw e;
		} finally {
			UtilsDao.closeDAOResources(rs, stmt, connection, logger);
		}

		return stockList;
	}

	public StockDTO getStockById(String id) throws Exception {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StockDTO resultStock = null;

		try {
			String sql = "SELECT  c.name AS name, c.cpf AS cpf, p.name AS product, p.unit AS unit, st.quantity AS quantity, st.id"
					+ " FROM cm_system.stock AS st" 
					+ " INNER JOIN  clients c ON st.client_id = c.id"
					+ " INNER JOIN products p ON st.product_id = p.id" + " WHERE st.id = ?";

			connection = ConnectionFactory.conectar();

			stmt = connection.prepareStatement(sql);
			stmt.setLong(1, Long.parseLong(id));
			rs = stmt.executeQuery();

			if (!rs.next()) {
				return resultStock;
			}

			resultStock = new StockDTO();
			resultStock.setName(rs.getString("name"));
			resultStock.setProduct(rs.getString("product"));
			resultStock.setUnitByProduct(rs.getString("product"));
			resultStock.setCpf(rs.getString("cpf"));
			resultStock.setNumQuantity(rs.getDouble("quantity"));

		} catch (Exception e) {
			logger.error("getStockById(String) -> Erro ao buscar o estoque por cliente", e);
			throw e;

		} finally {
			UtilsDao.closeDAOResources(rs, stmt, connection, logger);
		}

		return resultStock;
	}

	public StockDTO getStockById(Long id) throws Exception {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		StockDTO resultStock = null;

		try {
			String sql = "SELECT  c.name AS name, c.cpf AS cpf, p.name AS product, p.unit AS unit,"
					+ " st.quantity AS quantity, st.id" + " FROM cm_system.stock AS st"
					+ " INNER JOIN  clients c ON st.client_id = c.id" + " INNER JOIN products p ON st.product_id = p.id"
					+ " WHERE st.id = ?";

			connection = ConnectionFactory.conectar();

			stmt = connection.prepareStatement(sql);
			stmt.setLong(1, id);
			rs = stmt.executeQuery();

			if (!rs.next()) {
				return resultStock;
			}

			resultStock = new StockDTO();
			resultStock.setName(rs.getString("name"));
			resultStock.setProduct(rs.getString("product"));
			resultStock.setUnitByProduct(rs.getString("product"));
			resultStock.setCpf(rs.getString("cpf"));
			resultStock.setNumQuantity(rs.getDouble("quantity"));

		} catch (Exception e) {
			logger.error("getStockById(Long) -> Erro ao buscar o estoque por cliente", e);
			throw e;
		} finally {
			UtilsDao.closeDAOResources(rs, stmt, connection, logger);
		}

		return resultStock;
	}

	/**
	 * 
	 * @param stockId
	 * @param newQuantity
	 * @param pendingQuantity (is nullable)
	 * @return
	 * @throws Exception
	 */
	public boolean updateStock(String stockId, Double newQuantity, Double pendingQuantity) throws Exception {

		Connection connection = null;
		PreparedStatement stmt = null;
		boolean updated = false;

		try {
			connection = ConnectionFactory.conectar();

			String sql = "UPDATE cm_system.stock  SET quantity = ? WHERE id = ?";

			stmt = connection.prepareStatement(sql);
			stmt.setDouble(1, newQuantity);
			stmt.setLong(2, Long.parseLong(stockId));

			int rowsAffected = stmt.executeUpdate();
			updated = rowsAffected > 0;

		} catch (Exception e) {
			logger.error("updateStock() -> Erro ao atualizar o estoque após compra", e);
			throw e;

		} finally {
			UtilsDao.closeDAOResources(null, stmt, connection, logger);
		}

		return updated;
	}

	public Map<String, Object> getTotalCoffeAndPepper() throws Exception {
		Map<String, Object> result = new HashMap<>();

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		for (int i = 1; i < 3; i++) {
			String key = "";

			try {
				String sql = "SELECT SUM(quantity) FROM cm_system.stock WHERE product_id = " + i;
				connection = ConnectionFactory.conectar();
				stmt = connection.prepareStatement(sql);
				rs = stmt.executeQuery();

				while (rs.next()) {
					key = "cafe";
					if (i > 1)
						key = "pimenta";
					String formatedValue = PtBr.formatDecimal(rs.getDouble(1));
					result.put(key, formatedValue);
				}

			} catch (Exception e) {
				logger.info("getTotalCoffeAndPepper() -> Erro ao buscar o estoque de {}. {}", key, e.getMessage());
				throw e;

			} finally {
				UtilsDao.closeDAOResources(rs, stmt, connection, logger);
			}
		}

		if (result.size() > 1) {
			return result;
		} else {
			throw new AtomicStockQueryException("A atomicidade não foi bem sucedida no banco de dados");
		}
	}

	public List<StockDTO> getStockOwner() throws Exception {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<StockDTO> stockList = new ArrayList<>();

		try {

			String sql = "SELECT  c.name AS name, p.name AS product, p.unit AS unit, st.quantity AS quantity, st.id"
					+ " FROM cm_system.stock AS st" + " INNER JOIN  clients c ON st.client_id = c.id"
					+ " INNER JOIN products p ON st.product_id = p.id" + " WHERE c.id = ?";

			connection = ConnectionFactory.conectar();

			stmt = connection.prepareStatement(sql);
			stmt.setLong(1, Const.OWNER_ID);
			rs = stmt.executeQuery();

			while (rs.next()) {
				StockDTO stock = new StockDTO();
				stock.setId(rs.getLong("id"));
				stock.setName(rs.getString("name"));
				stock.setProduct(rs.getString("product"));
				stock.setNumQuantity(rs.getDouble("quantity"));
				stock.setUnitByProduct(rs.getString("product"));

				stockList.add(stock);
			}

		} catch (Exception e) {
			logger.error("getAllStocksByClientName() -> Erro ao buscar todos os estoques", e);
			throw e;

		} finally {
			UtilsDao.closeDAOResources(rs, stmt, connection, logger);
		}

		return stockList;
	}

	public Stock getStockOwner(Long productID) throws Exception {

		Stock stock = null;

		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;

		try {

			String sql = "SELECT  id, quantity FROM cm_system.stock" 
			+ " WHERE client_id = ? AND product_id = ?;";

			connection = ConnectionFactory.conectar();

			stmt = connection.prepareStatement(sql);
			stmt.setLong(1, Const.OWNER_ID);
			stmt.setLong(2, productID);
			rs = stmt.executeQuery();

			if (rs.next()) {
				stock = new Stock();
				stock.setId(rs.getLong("id"));
				stock.setQuantity(rs.getDouble("quantity"));
			}

		} catch (Exception e) {
			logger.error("getStockOwner() -> Erro ao buscar o stock owner", e);
			throw e;

		} finally {
			UtilsDao.closeDAOResources(rs, stmt, connection, logger);
		}

		return stock;
	}

	public List<StockDTO> getSupplyStocks(Long productID) throws Exception {
		Connection connection = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<StockDTO> stockList = new ArrayList<>();

		try {

			String sql = "SELECT  c.name AS name, c.id AS client_id, p.name AS product, p.unit AS unit,"
					+ " st.quantity AS quantity, st.id" + " FROM cm_system.stock AS st"
					+ " INNER JOIN  clients c ON st.client_id = c.id" + " INNER JOIN products p ON st.product_id = p.id"
					+ " WHERE st.product_id = ? AND quantity > 0 AND client_id != ?";

			connection = ConnectionFactory.conectar();

			stmt = connection.prepareStatement(sql);
			stmt.setLong(1, productID);
			stmt.setLong(2, Const.OWNER_ID);
			rs = stmt.executeQuery();

			while (rs.next()) {
				StockDTO stock = new StockDTO();
				stock.setId(rs.getLong("id"));
				stock.setName(rs.getString("name"));
				stock.setClientID(rs.getLong("client_id"));
				stock.setProduct(rs.getString("product"));
				stock.setNumQuantity(rs.getDouble("quantity"));
				stock.setUnitByProduct(rs.getString("product"));

				stockList.add(stock);
			}

		} catch (Exception e) {
			logger.error("getSupplyStocks() -> Erro ao buscar todos os estoques", e);
			throw e;
		} finally {
			UtilsDao.closeDAOResources(rs, stmt, connection, logger);
		}

		return stockList;
	}

}
