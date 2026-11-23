package cl.dssm.rrff.gantt;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;
import java.util.*;
import org.springframework.stereotype.Component;

/** R05: immutable and explicit allow-list for schema, endpoints, and JSON-to-SQL fields. */
@Component
public class R72Catalog {
  public record Field(String db, String json, String type) {}
  public record Spec(String slug, String table, String id, List<Field> columns) {
    public Field byJson(String name) {
      return columns.stream().filter(c -> c.json().equals(name)).findFirst().orElse(null);
    }
    public Field byDb(String name) {
      return columns.stream().filter(c -> c.db().equals(name)).findFirst().orElse(null);
    }
  }
  private final Map<String, Spec> definitions;
  public R72Catalog(ObjectMapper mapper) throws Exception {
    Map<String, Spec> out = new LinkedHashMap<>();
    try (InputStream is = Objects.requireNonNull(getClass().getResourceAsStream("/rrff72/catalog.json"))) {
      JsonNode entries = mapper.readTree(is);
      for (JsonNode t : entries) {
        List<Field> cols = new ArrayList<>();
        for (JsonNode f : t.get("columns")) cols.add(new Field(f.get("db").asText(),f.get("json").asText(),f.get("type").asText()));
        Spec spec = new Spec(t.get("slug").asText(), t.get("table").asText(),t.get("id").asText(), List.copyOf(cols));
        if (!spec.table().matches("r72_[a-z_]+") || !spec.id().matches("[a-z_]+") ||
          spec.columns().stream().anyMatch(f -> !f.db().matches("[a-z_]+")))
          throw new IllegalArgumentException("Invalid static R05 metadata");
        out.put(spec.slug(), spec);
      }
    }
    this.definitions = Map.copyOf(out);
  }
  public Spec require(String slug) {
    Spec result=definitions.get(slug);
    if(result==null) throw new org.springframework.web.server.ResponseStatusException(org.springframework.http.HttpStatus.NOT_FOUND,"Recurso R05 desconocido");
    return result;
  }
  public Set<String> names() { return definitions.keySet(); }
}
