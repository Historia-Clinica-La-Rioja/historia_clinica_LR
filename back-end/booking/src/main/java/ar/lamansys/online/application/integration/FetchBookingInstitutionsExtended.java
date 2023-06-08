package ar.lamansys.online.application.integration;

import ar.lamansys.online.domain.integration.BookingInstitutionBo;
import ar.lamansys.online.domain.integration.BookingInstitutionExtendedBo;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class FetchBookingInstitutionsExtended {

    private final BookingInstitutionStorage bookingInstitutionStorage;

    public FetchBookingInstitutionsExtended(BookingInstitutionStorage bookingInstitutionStorage) {
        this.bookingInstitutionStorage = bookingInstitutionStorage;
    }

    public List<BookingInstitutionExtendedBo> run() {
        return bookingInstitutionStorage.findBookingInstitutionsExtended();
    }
}
