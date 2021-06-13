package net.pladema.hl7.dataexchange.model.adaptor;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Cast {

    public static String toString(Object o){
        return o != null ? String.valueOf(o) : null;
    }

    public static java.sql.Date toSqlDate(Object o){
        return o != null ? (java.sql.Date) o : null;
    }

    public static LocalDate toLocalDate(String o) {
        return o != null ? LocalDate.parse(o) : null;
    }
}
