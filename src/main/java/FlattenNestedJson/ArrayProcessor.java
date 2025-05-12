package FlattenNestedJson;

import java.util.List;
import java.util.Map;

// Strategy for processing arrays
class ArrayProcessor implements JSONProcessor {
  @Override
  public void process(String prefix, Object value, Map<String, String> result) {
     if (value instanceof List) {
      List<Object> list = (List<Object>) value;
      for (int i = 0; i < list.size(); i++) {
        String newKey = prefix + "[" + i + "]";
        JSONFlattener.process(newKey, list.get(i), result);
      }
    }
  }
}