package net.pladema.sgx.repository.dialect;

import org.hibernate.dialect.SQLServerDialect;
import org.hibernate.dialect.function.SQLFunctionTemplate;
import org.hibernate.type.StandardBasicTypes;

public class CustomSQLServerDialect extends SQLServerDialect {

    public CustomSQLServerDialect() {
        super();
        registerFunction("array_agg", new SQLFunctionTemplate( StandardBasicTypes.STRING, "string_agg(?1,',')" ));
    }

}
