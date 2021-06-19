package ar.lamansys.sgh.shared.infrastructure.input.service;


import java.util.Optional;

public interface SharedStaffPort {

    Optional<ClinicalSpecialtyDto> getClinicalSpecialty(Integer clinicalSpecialtyId);

}
