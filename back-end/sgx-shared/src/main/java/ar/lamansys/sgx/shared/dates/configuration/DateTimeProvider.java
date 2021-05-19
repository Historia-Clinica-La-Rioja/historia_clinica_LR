package ar.lamansys.sgx.shared.dates.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Class to mock date in unit test
 */
@Component
public class DateTimeProvider {

    private static final Logger LOG = LoggerFactory.getLogger(DateTimeProvider.class);

    public static final String OUTPUT = "Output -> {}";

    public LocalDateTime nowDateTime(){
        LocalDateTime result = LocalDateTime.now();
        LOG.debug(OUTPUT, result);
        return result;
    }

    public LocalDate nowDate(){
        LocalDate result = LocalDate.now();
        LOG.debug(OUTPUT, result);
        return result;
    }

    public LocalDateTime nowDateTimeWithZone(ZoneId zoneId) {
        LocalDateTime localDateTime = LocalDateTime.now()
                .atZone(ZoneId.of(JacksonDateFormatConfig.UTC_ZONE_ID))
                .withZoneSameInstant(zoneId)
                .toLocalDateTime();
        LOG.debug(OUTPUT, localDateTime);
        return localDateTime;
    }
}
