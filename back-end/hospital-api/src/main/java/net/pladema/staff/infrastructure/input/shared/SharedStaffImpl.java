package net.pladema.staff.infrastructure.input.shared;

import ar.lamansys.sgh.shared.infrastructure.input.service.ClinicalSpecialtyDto;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedStaffPort;
import net.pladema.staff.service.ClinicalSpecialtyService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SharedStaffImpl implements SharedStaffPort {

    private final HealthcareProfessionalExternalService healthcareProfessionalExternalService;

    private final ClinicalSpecialtyService clinicalSpecialtyService;

    public SharedStaffImpl(HealthcareProfessionalExternalService healthcareProfessionalExternalService,
                           ClinicalSpecialtyService clinicalSpecialtyService) {
        this.healthcareProfessionalExternalService = healthcareProfessionalExternalService;
        this.clinicalSpecialtyService = clinicalSpecialtyService;
    }

    @Override
    public Integer getProfessionalId(Integer userId) {
        return healthcareProfessionalExternalService.getProfessionalId(userId);
    }

    @Override
    public Optional<ClinicalSpecialtyDto> getClinicalSpecialty(Integer clinicalSpecialtyId) {
        return clinicalSpecialtyService.getClinicalSpecialty(clinicalSpecialtyId)
                .map(clinicalSpecialtyBo -> new ClinicalSpecialtyDto(clinicalSpecialtyBo.getId(), clinicalSpecialtyBo.getName()));
    }
}
