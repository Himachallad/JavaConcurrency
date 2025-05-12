package FlattenNestedJson;

import java.util.Map;

// Strategy for processing objects
class ObjectProcessor implements JSONProcessor {
  @Override
  public void process(String prefix, Object value, Map<String, String> result) {
    if (value instanceof Map) {
      for (Map.Entry<String, Object> entry : ((Map<String, Object>) value).entrySet()) {
        String newKey = prefix.isEmpty() ? entry.getKey() : prefix + "." + entry.getKey();
        JSONFlattener.process(newKey, entry.getValue(), result);
      }
    }
  }
}
