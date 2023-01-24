package net.pladema.clinichistory.requests.medicationrequests.service.impl;

import ar.lamansys.sgx.auth.user.infrastructure.output.user.User;
import ar.lamansys.sgx.auth.user.infrastructure.output.user.UserRepository;
import lombok.AllArgsConstructor;
import net.pladema.clinichistory.requests.medicationrequests.service.ValidateMedicationRequestGenerationService;

import net.pladema.person.repository.entity.PersonExtended;
import net.pladema.person.service.PersonService;

import net.pladema.staff.controller.dto.ProfessionalDto;
import net.pladema.staff.controller.dto.ProfessionalLicenseNumberValidationResponseDto;

import net.pladema.staff.service.HealthcareProfessionalService;

import net.pladema.staff.service.domain.HealthcareProfessionalBo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class ValidateMedicationRequestGenerationServiceImpl implements ValidateMedicationRequestGenerationService {

	private static final Logger LOG = LoggerFactory.getLogger(ValidateMedicationRequestGenerationServiceImpl.class);

	private final UserRepository userRepository;

	private final PersonService personService;

	private final HealthcareProfessionalService healthcareProfessionalService;

	@Override
	public ProfessionalLicenseNumberValidationResponseDto execute(Integer userId, ProfessionalDto healthcareProfessionalData) {
		LOG.debug("Input parameters -> userId {}, healthcareProfessional {}", userId, healthcareProfessionalData);
		ProfessionalLicenseNumberValidationResponseDto response = new ProfessionalLicenseNumberValidationResponseDto();
		validateTwoFactorAuthentication(userId, response);
		validateContactData(healthcareProfessionalData, response);
		return response;
	}

	private void validateTwoFactorAuthentication(Integer userId, ProfessionalLicenseNumberValidationResponseDto response) {
		LOG.debug("Input parameters -> userId {}", userId);
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

}
