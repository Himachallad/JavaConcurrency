package FlattenNestedJson;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class FlattenJsonJackson {
  public static void main(String[] args) throws IOException {
    String json = "{\"name\":\"John\",\"age\":30,\"address\":{\"street\":\"123 Main St\",\"city\":\"San Francisco\"}, \"a\": {\"b\": {\"c\": [1,2,3,4, {\"d\": 5}]}}}";

    ObjectMapper mapper = new ObjectMapper();
    JsonNode root = mapper.readTree(json);

    flatten(root, "");
  }

  private static void flatten(JsonNode node, String prefix) {
    if (node.isObject()) {
      for (Map.Entry<String, JsonNode> field : node.properties()) {
        flatten(field.getValue(), prefix + field.getKey() + ".");
      }
    } else if (node.isArray()) {
      for (int i = 0; i < node.size(); i++) {
        flatten(node.get(i), prefix + "[" + i + "].");
      }
    } else {
      System.out.println(prefix + ": " + node.asText());
    }
  }
}
