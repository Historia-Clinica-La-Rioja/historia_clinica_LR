package ar.lamansys.online.application.integration;

import ar.lamansys.online.domain.integration.BookingInstitutionBo;

import java.util.List;

public interface BookingInstitutionStorage {
    List<BookingInstitutionBo> findBookingInstitutions();
}
