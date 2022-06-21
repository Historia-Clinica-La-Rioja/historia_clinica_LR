package ar.lamansys.online.application.integration;

import java.util.List;

import org.springframework.stereotype.Service;

import ar.lamansys.online.domain.integration.BookingInstitutionBo;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class FetchBookingInstitutions {

    private final BookingInstitutionStorage bookingInstitutionStorage;

    public FetchBookingInstitutions(BookingInstitutionStorage bookingInstitutionStorage) {
        this.bookingInstitutionStorage = bookingInstitutionStorage;
    }

    public List<BookingInstitutionBo> run() {
        return bookingInstitutionStorage.findBookingInstitutions();
    }
}
