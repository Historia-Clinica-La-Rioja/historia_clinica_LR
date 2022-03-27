package ar.lamansys.sgx.shared.repositories.dialect;

import ar.lamansys.sgx.shared.repositories.dialect.function.PostgreSQLFullTextFunction;
import ar.lamansys.sgx.shared.repositories.dialect.type.ObjectArrayUserType;
import org.hibernate.dialect.PostgreSQL95Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.DoubleType;
import org.hibernate.type.ObjectType;

import java.sql.Types;

public class CustomPostgreSQLDialect extends PostgreSQL95Dialect {

    public CustomPostgreSQLDialect() {
        super();
        registerHibernateType(Types.ARRAY, ObjectArrayUserType.INSTANCE.getClass().getName());

        // Full text search functions
        registerFunction("fts", new PostgreSQLFullTextFunction());
        registerFunction("ts_rank", new StandardSQLFunction("ts_rank", DoubleType.INSTANCE));
        registerFunction("to_tsquery", new StandardSQLFunction("to_tsquery", ObjectType.INSTANCE));
        registerFunction("plainto_tsquery", new StandardSQLFunction("plainto_tsquery", ObjectType.INSTANCE));
        registerFunction("to_tsvector", new StandardSQLFunction("to_tsvector", ObjectType.INSTANCE));

    }
}
