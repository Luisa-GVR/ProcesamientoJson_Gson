import com.google.gson.*;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;

public class PC {

    public static void main(String[] args) {
        try {
            String xmlFile = "car_sales.xml";

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newDefaultInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(new File(xmlFile));

            JsonObject rootObject = new JsonObject();
            JsonArray saleArray = convertDocumentToJson(doc);
            rootObject.add("car_sales", saleArray);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString = gson.toJson(rootObject);
            System.out.println(jsonString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static JsonArray convertDocumentToJson(Document doc) {
        JsonArray saleArray = new JsonArray();
        NodeList saleNodes = doc.getElementsByTagName("sale");

        for (int i = 0; i < saleNodes.getLength(); i++) {
            Element saleElement = (Element) saleNodes.item(i);
            JsonObject saleObject = convertElementToJson(saleElement);
            saleArray.add(saleObject);
        }

        return saleArray;
    }

    public static JsonObject convertElementToJson(Element element) {
        JsonObject jsonObject = new JsonObject();

        NamedNodeMap attributes = element.getAttributes();
        for (int i = 0; i < attributes.getLength(); i++) {
            Node attribute = attributes.item(i);
            jsonObject.addProperty(attribute.getNodeName(), attribute.getNodeValue());
        }

        NodeList nodeList = element.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);

            if (node.getNodeType() == Node.ELEMENT_NODE) {
                Element childElement = (Element) node;
                JsonObject childObject = convertElementToJson(childElement);

                if (childObject.entrySet().size() == 1) {
                    String childAttributeName = childObject.entrySet().iterator().next().getKey();
                    jsonObject.addProperty(childAttributeName, childObject.get(childAttributeName).getAsString());
                } else {
                    jsonObject.add(childElement.getNodeName(), childObject);
                }
            } else if (node.getNodeType() == Node.TEXT_NODE) {
                String text = node.getNodeValue().trim();
                if (!text.isEmpty()) {
                    jsonObject.addProperty(element.getNodeName(), text);
                }
            }
        }

        return jsonObject;
    }


}
