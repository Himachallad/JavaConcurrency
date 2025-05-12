package FlattenNestedJson;

import java.util.Map;

public class FlattenJSON {
  public static void main(String[] args) {
    String json = "{ \"name\": \"John\", \"address\": { \"city\": \"New York\", \"zipcode\": \"10001\", \"geo\": { \"lat\": \"40.7128\", \"lng\": \"-74.0060\" } }, \"age\": 30, \"hobbies\": [\"reading\", \"cycling\"], \"skills\": [{ \"name\": \"Java\", \"level\": \"expert\" }, { \"name\": \"Python\", \"level\": \"intermediate\" }] }";

    // Parse JSON
    Map<String, Object> parsedJson = JSONParser.parseJson(json);

    // Flatten JSON
    Map<String, String> flattenedJson = JSONFlattener.flatten(parsedJson);

    // Print result
    for (Map.Entry<String, String> entry : flattenedJson.entrySet()) {
      System.out.println(entry.getKey() + " : " + entry.getValue());
    }
  }
}