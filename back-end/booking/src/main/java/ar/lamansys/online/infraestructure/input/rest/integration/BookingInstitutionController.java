package ar.lamansys.online.infraestructure.input.rest.integration;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import ar.lamansys.online.application.integration.FetchBookingInstitutions;
import ar.lamansys.sgh.shared.infrastructure.input.service.booking.BookingInstitutionDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/booking/integration")
public class BookingInstitutionController {

    private final FetchBookingInstitutions fetchBookingInstitutions;

    public BookingInstitutionController(FetchBookingInstitutions fetchBookingInstitutions) {
        this.fetchBookingInstitutions = fetchBookingInstitutions;
    }

    @GetMapping("/institution")
    public List<BookingInstitutionDto> getAllBookingInstitutions() {
        return fetchBookingInstitutions.run().stream()
				.map(institution -> new BookingInstitutionDto(institution.getId(), institution.getDescription()))
				.collect(Collectors.toList());
    }
}
