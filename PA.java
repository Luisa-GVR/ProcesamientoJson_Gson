import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.FileReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class PA {
    public static void main(String[] args) {

        String jsonFileString = "car_sales.json";
        try{
            FileReader fr = new FileReader(jsonFileString);
            Gson gson = new Gson();
            JsonArray carSalesArray = gson.fromJson(fr, JsonArray.class);
            Map<String, Double> precioPromedio = calcularPromedio(carSalesArray);

            System.out.println("Marca:\s Precio promedio:");
            for (Map.Entry<String, Double> entry : precioPromedio.entrySet()){
                String marca = entry.getKey();
                double promedio = entry.getValue();
                DecimalFormat df = new DecimalFormat("#.##");
                System.out.println(marca + ": " + df.format(promedio));
            }


        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private static Map<String, Double> calcularPromedio(JsonArray carSalesArray) {
        Map<String,Double> precioTotal = new HashMap<>();
        Map<String,Integer> contarMarca = new HashMap<>();

        //obtener elementos
        for (JsonElement element : carSalesArray){
            JsonObject carroVenta = element.getAsJsonObject();
            String marca = carroVenta.get("car").getAsString();
            Double precio = new Double(carroVenta.get("price").getAsString().replace("$", ""));

            precioTotal.put(marca, precioTotal.getOrDefault(marca, 0.0) + precio);
            contarMarca.put(marca, contarMarca.getOrDefault(marca, 0) + 1);


        }
        //promediar
        Map<String, Double> precioPromedio = new HashMap<>();
        for (Map.Entry<String, Double> entry : precioTotal.entrySet()){
            String marca = entry.getKey();
            double precioTotal2 = entry.getValue();
            int cuenta = contarMarca.get(marca);
            double precioPromedioFinal = precioTotal2 / cuenta;
            precioPromedio.put(marca, precioPromedioFinal);

        }
        return  precioPromedio;

    }
}
