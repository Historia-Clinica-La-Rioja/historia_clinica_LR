package net.pladema.sgx.repository.dialect;

import net.pladema.sgx.repository.dialect.type.ObjectArrayUserType;
import org.hibernate.dialect.PostgreSQL95Dialect;

import java.sql.Types;

public class CustomPostgreSQLDialect extends PostgreSQL95Dialect {

    public CustomPostgreSQLDialect() {
        super();
        registerHibernateType(Types.ARRAY, ObjectArrayUserType.INSTANCE.getClass().getName());
    }
}
