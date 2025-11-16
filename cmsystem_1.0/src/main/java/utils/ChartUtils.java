package utils;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;

public class ChartUtils {

	public static String generateJsonChartData(List<BigDecimal> coffeeProfitList, List<BigDecimal> pepperProfitList)
					throws IOException  {
		
		Map<String, Object> chartData = new LinkedHashMap<>();
		
		  List<String> labels = new ArrayList<String>();
		  Map<String, Object> cafeDataset = new HashMap<String, Object>();
		  Map<String, Object> pimentaDataset= new HashMap<String, Object>();

		// Labels lista do nome dos clientes
		chartData.put("labels", labels);

		// Dataset 1 - Lucro Café
		cafeDataset.put("label", "Lucro Café (%)");
		cafeDataset.put("data", coffeeProfitList); //lucros do café
		cafeDataset.put("fill", true);
		cafeDataset.put("borderColor", "green");
		cafeDataset.put("backgroundColor", "rgba(144, 238, 144, 0.3)");
		cafeDataset.put("tension", 0.3);
		cafeDataset.put("pointBackgroundColor", "green");

		// Dataset 2 - Lucro Pimenta
		pimentaDataset.put("label", "Lucro Pimenta (%)");
		pimentaDataset.put("data", pepperProfitList);//lucros pimenta
		pimentaDataset.put("fill", false);
		pimentaDataset.put("borderColor", "red");
		pimentaDataset.put("backgroundColor", "red");
		pimentaDataset.put("tension", 0.3);
		pimentaDataset.put("pointBackgroundColor", "red");

		// Datasets list
		List<Map<String, Object>> datasets = new ArrayList<>();
		datasets.add(cafeDataset);
		datasets.add(pimentaDataset);

		chartData.put("datasets", datasets);

		// Converter para JSON
		Gson gson = new Gson();
		return gson.toJson(chartData);
	}
}

/* MODELO DO JSON GERADO:
 {
  "labels": [
    "Venda 1",
    "Venda 2",
    "Venda 3",
    "Venda 4",
    "Venda 5",
    "Venda 6",
    "Venda 7",
    "Venda 8",
    "Venda 9",
    "Venda 10"
  ],
  "datasets": [
    {
      "label": "Lucro Café (%)",
      "data": [22, 30, 18, 45, 35, 50, 28, 33, 40, 25],
      "fill": true,
      "borderColor": "green",
      "backgroundColor": "rgba(144, 238, 144, 0.3)",
      "tension": 0.3,
      "pointBackgroundColor": "green"
    },
    {
      "label": "Lucro Pimenta (%)",
      "data": [15, 25, 20, 30, 27, 35, 22, 26, 31, 18],
      "fill": false,
      "borderColor": "red",
      "backgroundColor": "red",
      "tension": 0.3,
      "pointBackgroundColor": "red"
    }
  ]
}

 */
