package ar.lamansys.online.application.insurance;

import ar.lamansys.online.domain.insurance.BookingHealthInsuranceBo;

import java.util.List;
import java.util.Optional;

public interface BookingHealthcareInsuranceStorage {
    Optional<List<BookingHealthInsuranceBo>> findHealthcareInsurances();
}
