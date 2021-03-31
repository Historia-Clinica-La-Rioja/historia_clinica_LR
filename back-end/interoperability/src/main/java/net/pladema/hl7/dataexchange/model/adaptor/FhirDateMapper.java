package net.pladema.hl7.dataexchange.model.adaptor;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@UtilityClass
public class FhirDateMapper {

    public static LocalDate toLocalDate(java.util.Date date){
        if(date != null){
            try{
                return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            } catch (UnsupportedOperationException ex){
                return toLocalDate((java.sql.Date) date);
            }
        }
        return null;
    }

    public static LocalDate toLocalDate(java.sql.Date date){
        return date != null ? date.toLocalDate() : null;
    }

    public static LocalDate toLocalDate(String date){
        if(date != null){
            return LocalDate.parse(date);
        }
        return null;
    }

    public static LocalDateTime toLocalDateTime(java.sql.Timestamp date){
        return date != null ? date.toLocalDateTime() : null;
    }

    public static LocalDateTime toLocalDateTime(java.util.Date date){
        return date != null ?
                new java.sql.Timestamp(date.getTime()).toLocalDateTime() :
                null;
    }

    public static java.util.Date toDate(LocalDateTime date){
        return date != null ?
                Date.from(date.atZone(ZoneId.systemDefault()).toInstant()) :
                null;
    }

    public static java.util.Date toDate(LocalDate date){
        return date != null ?
                Date.from(date.atStartOfDay().atZone(ZoneId.systemDefault()).toInstant()) :
                null;
    }
}
