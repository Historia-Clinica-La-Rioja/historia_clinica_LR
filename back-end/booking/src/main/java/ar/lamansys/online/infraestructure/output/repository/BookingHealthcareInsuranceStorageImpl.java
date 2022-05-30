package ar.lamansys.online.infraestructure.output.repository;

import ar.lamansys.online.application.insurance.BookingHealthcareInsuranceStorage;
import ar.lamansys.online.domain.insurance.BookingHealthInsuranceBo;
import ar.lamansys.online.infraestructure.output.entity.VBookingHealthInsurance;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingHealthcareInsuranceStorageImpl implements BookingHealthcareInsuranceStorage {

    private final BookingHealthInsuranceJpaRepository repository;

    public BookingHealthcareInsuranceStorageImpl(BookingHealthInsuranceJpaRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<List<BookingHealthInsuranceBo>> findHealthcareInsurances() {
        return Optional.of(repository.findAll().stream().map(this::mapTo).collect(Collectors.toList()));
    }

    private BookingHealthInsuranceBo mapTo(VBookingHealthInsurance insurance) {
        return new BookingHealthInsuranceBo(insurance.getId(),
                insurance.getDescription());
    }
}
