package net.pladema.staff.infrastructure.input.shared;

import java.util.Optional;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.shared.infrastructure.input.service.ClinicalSpecialtyDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.ProfessionalInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedStaffPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.staff.ProfessionalCompleteDto;
import net.pladema.staff.controller.mapper.ClinicalSpecialtyMapper;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;
import net.pladema.staff.service.ClinicalSpecialtyService;

@Service
public class SharedStaffImpl implements SharedStaffPort {

    private final HealthcareProfessionalExternalService healthcareProfessionalExternalService;

    private final ClinicalSpecialtyService clinicalSpecialtyService;

    private final ClinicalSpecialtyMapper clinicalSpecialtyMapper;

    public SharedStaffImpl(HealthcareProfessionalExternalService healthcareProfessionalExternalService,
                           ClinicalSpecialtyService clinicalSpecialtyService,
                           ClinicalSpecialtyMapper clinicalSpecialtyMapper) {
        this.healthcareProfessionalExternalService = healthcareProfessionalExternalService;
        this.clinicalSpecialtyService = clinicalSpecialtyService;
        this.clinicalSpecialtyMapper = clinicalSpecialtyMapper;
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

    @Override
    public ProfessionalInfoDto getProfessionalCompleteInfo(Integer userId) {
        return Optional.ofNullable(healthcareProfessionalExternalService.findProfessionalByUserId(userId))
                .map(professional -> {
                    var specialties = clinicalSpecialtyService.getSpecialtiesByProfessional(professional.getId());
                    return new ProfessionalInfoDto(professional.getId(), professional.getLicenceNumber(), professional.getFirstName(),
                            professional.getLastName(), professional.getIdentificationNumber(), professional.getPhoneNumber(),
                            clinicalSpecialtyMapper.fromListClinicalSpecialtyBo(specialties), professional.getNameSelfDetermination());
                })
                .get();
    }

	@Override
	public ProfessionalCompleteDto getProfessionalComplete(Integer userId) {
		return healthcareProfessionalExternalService.getProfessionalCompleteInfoByUser(userId);
	}

	@Override
	public ProfessionalCompleteDto getProfessionalCompleteById(Integer professionalId) {
		return healthcareProfessionalExternalService.getProfessionalCompleteInfoById(professionalId);
	}
}
