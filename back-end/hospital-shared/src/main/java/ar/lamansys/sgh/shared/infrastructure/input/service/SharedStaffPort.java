package ar.lamansys.sgh.shared.infrastructure.input.service;


import java.util.Optional;

import ar.lamansys.sgh.shared.infrastructure.input.service.staff.ProfessionalCompleteDto;

public interface SharedStaffPort {

    Integer getProfessionalId(Integer userId);

    Optional<ClinicalSpecialtyDto> getClinicalSpecialty(Integer clinicalSpecialtyId);

    ProfessionalInfoDto getProfessionalCompleteInfo(Integer userId);

	ProfessionalCompleteDto getProfessionalComplete(Integer userId);

	ProfessionalCompleteDto getProfessionalCompleteById(Integer professionalId);
}
