package ar.lamansys.sgh.shared.infrastructure.input.service;


import java.util.Optional;

public interface SharedStaffPort {

    Integer getProfessionalId(Integer userId);

    Optional<ClinicalSpecialtyDto> getClinicalSpecialty(Integer clinicalSpecialtyId);
}
