package FlattenNestedJson;

import java.util.List;
import java.util.Map;

interface JSONProcessor {
  void process(String prefix, Object value, Map<String, String> result);
}






