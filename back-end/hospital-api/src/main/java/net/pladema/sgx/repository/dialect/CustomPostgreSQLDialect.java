package net.pladema.sgx.repository.dialect;

import net.pladema.sgx.repository.dialect.type.ObjectArrayUserType;
import org.hibernate.dialect.PostgreSQL95Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.CustomType;
import org.hibernate.type.Type;

import java.sql.Types;

public class CustomPostgreSQLDialect extends PostgreSQL95Dialect {

    public CustomPostgreSQLDialect() {
        super();
        registerHibernateType(Types.ARRAY, ObjectArrayUserType.INSTANCE.getClass().getName());
        Type arrayType = new CustomType(ObjectArrayUserType.INSTANCE);
        registerFunction("array_agg", new StandardSQLFunction("array_agg", arrayType));
    }
}
