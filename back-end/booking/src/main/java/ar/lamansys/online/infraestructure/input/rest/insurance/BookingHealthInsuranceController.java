package ar.lamansys.online.infraestructure.input.rest.insurance;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.online.application.insurance.FetchHealthcareInsurances;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingHealthInsuranceDto;

@RestController
@RequestMapping("/booking")
public class BookingHealthInsuranceController {
    private static final Logger LOG = LoggerFactory.getLogger(BookingHealthInsuranceController.class);

    private final FetchHealthcareInsurances fetchHealthcareInsurances;

    public BookingHealthInsuranceController(FetchHealthcareInsurances fetchHealthcareInsurances) {
        this.fetchHealthcareInsurances = fetchHealthcareInsurances;
    }

    @GetMapping("/healthinsurances")
    public ResponseEntity<List<BookingHealthInsuranceDto>> getAllHealthInsurances() {
        List<BookingHealthInsuranceDto> result = new ArrayList<>();
        var insuranceBo = fetchHealthcareInsurances.run();
        insuranceBo.forEach(insurance -> result.add(new BookingHealthInsuranceDto(insurance.getId(), insurance.getName())));
        LOG.debug("Get all booking institutions => {}", result);
        return ResponseEntity.ok(result);
    }

}
