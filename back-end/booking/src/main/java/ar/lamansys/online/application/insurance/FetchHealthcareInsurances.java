package ar.lamansys.online.application.insurance;

import ar.lamansys.online.domain.insurance.BookingHealthInsuranceBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FetchHealthcareInsurances {

    private final Logger logger;

    private final BookingHealthcareInsuranceStorage bookingHealthcareInsuranceStorage;

    public FetchHealthcareInsurances(BookingHealthcareInsuranceStorage bookingHealthcareInsuranceStorage) {
        this.bookingHealthcareInsuranceStorage = bookingHealthcareInsuranceStorage;
        this.logger = LoggerFactory.getLogger(this.getClass());
    }

    public List<BookingHealthInsuranceBo> run() {
        logger.debug("Fetch healthcare insurances");
        var result = bookingHealthcareInsuranceStorage.findHealthcareInsurances()
                .orElse(new ArrayList<>());
        logger.trace("healthcare insurances result -> {}", result);
        return result;
    }
}
