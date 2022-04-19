package ar.lamansys.online.application.integration;

import ar.lamansys.online.domain.integration.BookingInstitutionBo;

import java.util.List;
import java.util.Optional;

public interface BookingInstitutionStorage {
    List<BookingInstitutionBo> findBookingInstitutions();
}
