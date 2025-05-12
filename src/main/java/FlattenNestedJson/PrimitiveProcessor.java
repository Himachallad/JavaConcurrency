package FlattenNestedJson;

import java.util.Map;

// Strategy for processing primitive values
class PrimitiveProcessor implements JSONProcessor {
  @Override
  public void process(String prefix, Object value, Map<String, String> result) {
    result.put(prefix, value.toString());
  }
}
