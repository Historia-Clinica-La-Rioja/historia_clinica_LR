package ar.lamansys.online.application.specialty;

import java.util.List;
import java.util.Optional;

import ar.lamansys.online.domain.specialty.BookingSpecialtyBo;
import ar.lamansys.online.domain.specialty.PracticeBo;

public interface BookingSpecialtyStorage {

    Optional<List<PracticeBo>> findBySpecialtyAndHealthInsurance(
            Integer clinicalSpecialtyId,
            Integer medicalCoverageId,
            boolean all
    );

    Optional<List<PracticeBo>> findByProfessionalAndHealthInsurance(
            Integer healthcareProfessionalId,
            Integer medicalCoverageId,
            Integer clinicalSpecialtyId,
            Boolean all
    );

    List<BookingSpecialtyBo> findAllSpecialties();

    Optional<List<BookingSpecialtyBo>> findAllSpecialtiesByProfessional(Integer healthcareProfessionalId);
}
