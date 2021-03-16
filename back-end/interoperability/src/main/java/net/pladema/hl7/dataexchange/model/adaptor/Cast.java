package net.pladema.hl7.dataexchange.model.adaptor;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Cast {

    public static String toString(Object o){
        return o != null ? String.valueOf(o) : null;
    }

    public static Integer toInteger(Object o){
        return o != null ? (Integer) o : null;
    }

    public static Short toShort(Object o) {
        return o != null ? (short)o : null;
    }

    public static Boolean toBoolean(Object o){
        return o != null ? (Boolean) o : Boolean.FALSE;
    }

    public static java.sql.Date toSqlDate(Object o){
        return o != null ? (java.sql.Date) o : null;
    }

    public static java.sql.Timestamp toSqlTimestamp(Object o){
        return o != null ? (java.sql.Timestamp) o : null;
    }

    public static LocalDate toLocalDate(Object o) {
        return o != null ? (LocalDate)o : null;
    }
}
