package ar.lamansys.sgx.shared.dates.configuration;

import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Class to mock date in unit test
 */
@Component
public class DateTimeProvider {

    public LocalDateTime nowDateTime(){
        LocalDateTime result = LocalDateTime.now();
        return result;
    }

    public LocalDate nowDate(){
        LocalDate result = LocalDate.now();
        return result;
    }

    public LocalDateTime nowDateTimeWithZone(ZoneId zoneId) {
        LocalDateTime localDateTime = LocalDateTime.now()
                .atZone(ZoneId.of(JacksonDateFormatConfig.UTC_ZONE_ID))
                .withZoneSameInstant(zoneId)
                .toLocalDateTime();
        return localDateTime;
    }
}
