package ar.lamansys.online.application.integration;

import ar.lamansys.online.domain.integration.BookingInstitutionBo;
import ar.lamansys.online.domain.integration.BookingInstitutionExtendedBo;

import java.util.List;

public interface BookingInstitutionStorage {
    List<BookingInstitutionBo> findBookingInstitutions();

	List<BookingInstitutionExtendedBo> findBookingInstitutionsExtended();
}
