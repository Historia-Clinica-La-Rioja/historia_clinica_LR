package ar.lamansys.sgx.shared.repositories.dialect;

import ar.lamansys.sgx.shared.repositories.dialect.type.ObjectArrayUserType;
import org.hibernate.dialect.DB2Dialect;
import org.hibernate.dialect.function.StandardSQLFunction;
import org.hibernate.type.CustomType;
import org.hibernate.type.Type;

import java.sql.Types;

public class CustomDB2Dialect extends DB2Dialect {

    public CustomDB2Dialect() {
        super();
        registerHibernateType(Types.ARRAY, ObjectArrayUserType.INSTANCE.getClass().getName());
        Type arrayType = new CustomType(ObjectArrayUserType.INSTANCE);
        registerFunction("array_agg", new StandardSQLFunction("array_agg", arrayType));
    }

}
