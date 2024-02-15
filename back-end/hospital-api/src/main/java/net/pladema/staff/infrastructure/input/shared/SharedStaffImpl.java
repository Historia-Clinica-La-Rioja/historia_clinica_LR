package net.pladema.staff.infrastructure.input.shared;

import java.util.Optional;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;

import lombok.extern.slf4j.Slf4j;

import net.pladema.hl7.dataexchange.model.adaptor.FhirString;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.shared.infrastructure.input.service.ClinicalSpecialtyDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.ProfessionalInfoDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedStaffPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.staff.ProfessionalCompleteDto;
import net.pladema.staff.controller.mapper.ClinicalSpecialtyMapper;
import net.pladema.staff.controller.service.HealthcareProfessionalExternalService;
import net.pladema.staff.service.ClinicalSpecialtyService;

@Service
@Slf4j
public class SharedStaffImpl implements SharedStaffPort {

    private final HealthcareProfessionalExternalService healthcareProfessionalExternalService;

    private final ClinicalSpecialtyService clinicalSpecialtyService;

    private final ClinicalSpecialtyMapper clinicalSpecialtyMapper;

	private final FeatureFlagsService featureFlagsService;

    public SharedStaffImpl(HealthcareProfessionalExternalService healthcareProfessionalExternalService,
                           ClinicalSpecialtyService clinicalSpecialtyService,
                           ClinicalSpecialtyMapper clinicalSpecialtyMapper,
						   FeatureFlagsService featureFlagsService) {
        this.healthcareProfessionalExternalService = healthcareProfessionalExternalService;
        this.clinicalSpecialtyService = clinicalSpecialtyService;
        this.clinicalSpecialtyMapper = clinicalSpecialtyMapper;
		this.featureFlagsService = featureFlagsService;
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
                            clinicalSpecialtyMapper.fromListClinicalSpecialtyBo(specialties), professional.getNameSelfDetermination(),
							professional.getMiddleNames(), professional.getOtherLastNames());
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

	@Override
	public Optional<String> getProfessionalCompleteNameByUserId(Integer userId){
		log.debug("Input parameteres -> userId {}", userId);
		ProfessionalCompleteDto professionalInfo = healthcareProfessionalExternalService.getProfessionalCompleteInfoByUser(userId);
		Optional<String> result = getCompleteName(professionalInfo);
		log.debug("Output -> result");
		return result;
	}

	private Optional<String> getCompleteName(ProfessionalCompleteDto professionalInfo){
		if (professionalInfo == null)
			return Optional.empty();
		String name = featureFlagsService.isOn(AppFeature.HABILITAR_DATOS_AUTOPERCIBIDOS) ? professionalInfo.getNameSelfDetermination() : FhirString.joining(professionalInfo.getFirstName(), professionalInfo.getMiddleNames());
		String completeName = professionalInfo.getCompleteName(name);
		return Optional.of(completeName);
	}

}
