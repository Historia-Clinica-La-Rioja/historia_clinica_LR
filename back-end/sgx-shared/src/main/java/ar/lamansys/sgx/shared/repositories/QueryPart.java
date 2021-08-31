package ar.lamansys.sgx.shared.repositories;

import lombok.NoArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import javax.persistence.Query;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor
public class QueryPart {

    private String query = Strings.EMPTY;
    private Map<String, Object> params = new HashMap<>();

    public QueryPart(String query){
        this.concat(query);
    }

    public QueryPart addParam(String name, Object value) {
        this.params.put(name, value);
        return this;
    }

    public QueryPart concatPart(QueryPart other) {
        this.concat(other.getQuery());
        this.params.putAll(other.getParams());
        return this;
    }

    public QueryPart concat(String s) {
        this.query = this.query + " " + s + " ";
        return this;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public void configParams(Query query){
        getParams().forEach(query::setParameter);
    }

    private Map<String, Object> getParams() {
        return params;
    }

    public String toString() {
        return getQuery();
    }
}
