package FlattenNestedJson;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

class JSONFlattener {
  private static final Map<Class<?>, JSONProcessor> processors = new HashMap<>();

  static {
    processors.put(Map.class, new ObjectProcessor());
    processors.put(List.class, new ArrayProcessor());
  }

  public static void process(String prefix, Object value, Map<String, String> result) {
    if (value == null) return;

    JSONProcessor processor = processors.getOrDefault(value.getClass(), new PrimitiveProcessor());
    processor.process(prefix, value, result);
  }

  public static Map<String, String> flatten(Map<String, Object> json) {
    Map<String, String> result = new HashMap<>();
    process("", json, result);
    return result;
  }
}

