package net.pladema.clinichistory.requests.medicationrequests.service.impl;

import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgx.auth.user.infrastructure.output.user.User;
import ar.lamansys.sgx.auth.user.infrastructure.output.user.UserRepository;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import io.jsonwebtoken.lang.Assert;
import lombok.AllArgsConstructor;
import net.pladema.clinichistory.requests.medicationrequests.service.ValidateMedicationRequestGenerationService;

import net.pladema.person.repository.entity.PersonExtended;
import net.pladema.person.service.PersonService;

import net.pladema.sisa.refeps.controller.RefepsExternalService;
import net.pladema.sisa.refeps.services.domain.ValidatedLicenseNumberBo;
import net.pladema.sisa.refeps.services.exceptions.RefepsApiException;
import net.pladema.staff.application.getlicensenumberbyprofessional.GetLicenseNumberByProfessional;
import net.pladema.staff.controller.dto.ProfessionalDto;
import net.pladema.staff.controller.dto.ProfessionalLicenseNumberValidationResponseDto;

import net.pladema.staff.domain.ProfessionalLicenseNumberBo;
import net.pladema.staff.service.HealthcareProfessionalService;

import net.pladema.staff.service.domain.HealthcareProfessionalBo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ValidateMedicationRequestGenerationServiceImpl implements ValidateMedicationRequestGenerationService {

	private static final Logger LOG = LoggerFactory.getLogger(ValidateMedicationRequestGenerationServiceImpl.class);

	private final UserRepository userRepository;

	private final PersonService personService;

	private final HealthcareProfessionalService healthcareProfessionalService;

	private final RefepsExternalService refepsExternalService;

	private final GetLicenseNumberByProfessional getLicenseNumberByProfessional;

	private final FeatureFlagsService featureFlagsService;

	@Override
	public ProfessionalLicenseNumberValidationResponseDto execute(Integer userId, ProfessionalDto healthcareProfessionalData, BasicPatientDto patientBasicData) {
		LOG.debug("Input parameters -> userId {}, healthcareProfessional {}", userId, healthcareProfessionalData);
		ProfessionalLicenseNumberValidationResponseDto response = new ProfessionalLicenseNumberValidationResponseDto();
		validateTwoFactorAuthentication(userId, response);
		validateContactData(healthcareProfessionalData, response);
		validateHealthcareProfessionalLicenseNumber(healthcareProfessionalData, response);
		addPatientEmail(patientBasicData, response);
		return response;
	}

	private void validateTwoFactorAuthentication(Integer userId, ProfessionalLicenseNumberValidationResponseDto response) {
		LOG.debug("Input parameters -> userId {}", userId);
		Assert.isTrue(featureFlagsService.isOn(AppFeature.HABILITAR_2FA), "No cuenta con la funcionalidad de doble factor de autenticación, por lo que no puede recetar");
		Optional<User> user = userRepository.findById(userId);
		user.ifPresent(userData -> {
			if (!userData.getTwoFactorAuthenticationEnabled())
				response.setTwoFactorAuthenticationEnabled(false);
		});
	}

	private void validateContactData(ProfessionalDto healthcareProfessionalData, ProfessionalLicenseNumberValidationResponseDto response) {
		LOG.debug("Input parameters -> patientId {}", healthcareProfessionalData);
		HealthcareProfessionalBo healthcareProfessional = healthcareProfessionalService.findActiveProfessionalById(healthcareProfessionalData.getId());
		PersonExtended personData = personService.getPersonExtended(healthcareProfessional.getPersonId());
		if (personData.getEmail() == null || personData.getPhoneNumber() == null)
			response.setHealthcareProfessionalCompleteContactData(false);
	}

	private void validateHealthcareProfessionalLicenseNumber(ProfessionalDto healthcareProfessionalData, ProfessionalLicenseNumberValidationResponseDto response) {
		LOG.debug("Input parameters -> healthcareProfessional {}", healthcareProfessionalData);
		List<String> healthcareProfessionalLicenses = getLicenseNumberByProfessional.run(healthcareProfessionalData.getId()).stream()
				.map(ProfessionalLicenseNumberBo::getLicenseNumber).collect(Collectors.toList());
		if (!healthcareProfessionalLicenses.isEmpty()) {
			if (featureFlagsService.isOn(AppFeature.HABILITAR_VALIDACION_MATRICULAS_SISA)) {
				try {
					boolean result = refepsExternalService.validateLicenseNumber(healthcareProfessionalData.getIdentificationNumber(), healthcareProfessionalLicenses).stream().filter(ValidatedLicenseNumberBo::getIsValid).findFirst().isEmpty();
					response.setHealthcareProfessionalLicenseNumberValid(!result);
				} catch (RefepsApiException e) {
					throw new RuntimeException(e);
				}
			}
		}
		else
			response.setHealthcareProfessionalHasLicenses(false);
	}

	private void addPatientEmail(BasicPatientDto basicPatientData, ProfessionalLicenseNumberValidationResponseDto response) {
		PersonExtended person = personService.getPersonExtended(basicPatientData.getPerson().getId());
		response.setPatientEmail(person.getEmail());
	}

}
