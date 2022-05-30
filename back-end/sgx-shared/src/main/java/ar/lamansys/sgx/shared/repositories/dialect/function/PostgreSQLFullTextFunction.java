package ar.lamansys.sgx.shared.repositories.dialect.function;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.QueryException;
import org.hibernate.dialect.function.SQLFunction;
import org.hibernate.engine.spi.Mapping;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.type.BooleanType;
import org.hibernate.type.Type;

import java.util.List;

@Slf4j
public class PostgreSQLFullTextFunction implements SQLFunction {

    private static final String DEFAULT_LANGUAGE = "spanish";

    @Override
    public String render(Type type, List args, SessionFactoryImplementor sessionFactoryImplementor)
            throws QueryException {
        if (args.size() < 2) {
            throw new IllegalArgumentException("The function must be passed 2 arguments");
        }

        String field = (String) args.get(0);
        String value = (String) args.get(1);
        String fragment = "";
        fragment += "to_tsvector( '" + DEFAULT_LANGUAGE + "', " + field + ") @@ ";

        String plainto_tsquery = "plainto_tsquery( '" + DEFAULT_LANGUAGE + "', " + value + ")";
		String regex_replace = "regexp_replace(cast(" + plainto_tsquery + " as text), E'(\\'\\\\w+\\')', E'\\\\1:*', 'g')"; // adds :* to each search term
		fragment += "to_tsquery( '" + DEFAULT_LANGUAGE + "', " + regex_replace + " )";
        log.trace("fragment: " + fragment);
        return fragment;

    }

    @Override
    public Type getReturnType(Type columnType, Mapping mapping) throws QueryException {
        return new BooleanType();
    }

    @Override
    public boolean hasArguments() {
        return true;
    }

    @Override
    public boolean hasParenthesesIfNoArguments() {
        return false;
    }

}
