package ar.lamansys.sgx.shared.repositories.dialect.type;

import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.usertype.UserType;

import java.io.Serializable;
import java.sql.Array;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

public class ObjectArrayUserType implements UserType {

    public static final ObjectArrayUserType INSTANCE = new ObjectArrayUserType();

    @Override
    public int[] sqlTypes() {
        return new int[] { Types.ARRAY };
    }

    @Override
    public Class returnedClass() {
        return Object[].class;
    }

    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        if( x== null){
            return y== null;
        }
        return x.equals( y);
    }

    @Override
    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    public Object nullSafeGet(
            ResultSet rs,
            String[] names,
            SessionImplementor session,
            Object owner) throws HibernateException, SQLException {
        if (rs.wasNull()) {
            return null;
        }

        try
        {
            Object[] array = (Object[]) rs.getArray(names[0]).getArray();
            return array;
        }
        catch(NullPointerException ex)
        {
            return new Object[0];
        }
    }

    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        if (value == null) {
            st.setNull(index, Types.OTHER);
            return;
        }

        Object[] castObject = (Object[]) value;
        Array array = session.connection().createArrayOf("object", castObject);
        st.setArray(index, array);
    }

    @Override
    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    @Override
    public boolean isMutable() {
        return true;
    }

    @Override
    public Serializable disassemble(Object value) throws HibernateException {
        return (Object[])this.deepCopy( value);
    }

    @Override
    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return this.deepCopy( cached);
    }

    @Override
    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

    @Override
    public Object nullSafeGet(ResultSet rs, String[] strings, SharedSessionContractImplementor ssci, Object o) throws HibernateException, SQLException {
        if (rs.wasNull()) {
            return null;
        }
        try
        {
            Object[] array = (Object[]) rs.getArray(strings[0]).getArray();
            return array;
        }
        catch(NullPointerException ex)
        {
            return new Object[0];
        }
    }

    @Override
    public void nullSafeSet(PreparedStatement ps, Object o, int i, SharedSessionContractImplementor ssci) throws HibernateException, SQLException {
        if (o == null) {
            ps.setNull(i, Types.OTHER);
            return;
        }

        Object[] castObject = (Object[]) o;
        Array array = ssci.connection().createArrayOf("object", castObject);
        ps.setArray(i, array);
    }


}
