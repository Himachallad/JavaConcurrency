package FlattenNestedJson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

class JSONParser {
  public static Map<String, Object> parseJson(String json) {
    json = json.trim();
    if (!json.startsWith("{") || !json.endsWith("}")) {
        throw new IllegalArgumentException("Invalid JSON format");
    }

    Map<String, Object> result = new HashMap<>();
    json = json.substring(1, json.length() - 1).trim(); // Remove outer braces

    Stack<Character> stack = new Stack<>();
    StringBuilder key = new StringBuilder();
    StringBuilder value = new StringBuilder();
    boolean readingKey = true, insideQuotes = false;

    for (int i = 0; i < json.length(); i++) {
      char c = json.charAt(i);

      if (c == '"') {
        insideQuotes = !insideQuotes; // Toggle quotes state
      } else if (!insideQuotes && (c == '{' || c == '[')) {
        stack.push(c);
      } else if (!insideQuotes && (c == '}' || c == ']')) {
        stack.pop();
      }

        if (!insideQuotes && stack.isEmpty() && (c == ',' || i == json.length() - 1)) {
        if (i == json.length() - 1) {
          value.append(c);
        }

        String k = key.toString().trim().replace("\"", "");
        String v = value.toString().trim().replace("\"", "");

        if (v.startsWith("{")) {
          result.put(k, parseJson(v)); // Recursive parsing for objects
        } else if (v.startsWith("[")) {
          result.put(k, parseArray(v)); // Parsing for arrays
        } else {
          result.put(k, v);
        }

        key.setLength(0);
        value.setLength(0);
        readingKey = true;
        continue;
      }

      if (readingKey) {
        if (c == ':') {
          readingKey = false;
        } else {
          key.append(c);
        }
      } else {
        value.append(c);
      }
    }

    return result;
  }

  public static List<Object> parseArray(String arrayJson) {
    arrayJson = arrayJson.trim();
    if (!arrayJson.startsWith("[") || !arrayJson.endsWith("]")) {
      throw new IllegalArgumentException("Invalid array format");
    }

    List<Object> result = new ArrayList<>();
    arrayJson = arrayJson.substring(1, arrayJson.length() - 1).trim(); // Remove brackets

    Stack<Character> stack = new Stack<>();
    StringBuilder value = new StringBuilder();
    boolean insideQuotes = false;

    for (int i = 0; i < arrayJson.length(); i++) {
      char c = arrayJson.charAt(i);

      if (c == '"') {
        insideQuotes = !insideQuotes;
      } else if (!insideQuotes && (c == '{' || c == '[')) {
        stack.push(c);
      } else if (!insideQuotes && (c == '}' || c == ']')) {
        stack.pop();
      }

      if (!insideQuotes && stack.isEmpty() && (c == ',' || i == arrayJson.length() - 1)) {
        if (i == arrayJson.length() - 1) {
          value.append(c);
        }

        String v = value.toString().trim().replace("\"", "");

        if (v.startsWith("{")) {
          result.add(parseJson(v));
        } else if (v.startsWith("[")) {
          result.add(parseArray(v));
        } else {
          result.add(v);
        }

        value.setLength(0);
        continue;
      }

      value.append(c);
    }

    return result;
  }
}
