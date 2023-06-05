package net.pladema.patient.controller.constraints.validator;

import ar.lamansys.sgh.shared.infrastructure.input.service.BasicDataPersonDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.patient.enums.EAuditType;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import ar.lamansys.sgx.shared.security.UserInfo;
import net.pladema.patient.controller.constraints.PatientUpdateValid;
import net.pladema.patient.controller.dto.APatientDto;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.patient.repository.entity.PatientType;
import net.pladema.patient.service.PatientService;
import net.pladema.permissions.repository.enums.ERole;
import net.pladema.person.controller.service.PersonExternalService;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import net.pladema.user.application.getrolesbyuser.GetRolesByUser;
import net.pladema.user.infrastructure.input.rest.dto.UserRoleDto;
import net.pladema.user.infrastructure.input.rest.mapper.HospitalUserRoleMapper;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class PatientUpdateValidator implements ConstraintValidator<PatientUpdateValid, Object[]> {

    private final PatientService patientService;

    private final PersonExternalService personExternalService;

    private final FeatureFlagsService featureFlagsService;

	private final GetRolesByUser getRolesByUser;

	private final HospitalUserRoleMapper hospitalUserRoleMapper;

    public PatientUpdateValidator(PatientService patientService,
								  PersonExternalService personExternalService,
								  FeatureFlagsService featureFlagsService, GetRolesByUser getRolesByUser, HospitalUserRoleMapper hospitalUserRoleMapper){
        this.patientService = patientService;
        this.personExternalService = personExternalService;
        this.featureFlagsService = featureFlagsService;
		this.getRolesByUser = getRolesByUser;
		this.hospitalUserRoleMapper = hospitalUserRoleMapper;
	}

    @Override
    public void initialize(PatientUpdateValid constraintAnnotation) {
        // Nothing to do
    }

    @Override
    public boolean isValid(Object[] parameters, ConstraintValidatorContext context) {

        Integer patientId = (Integer) parameters[0];
		Integer institutionId = (Integer) parameters[1];
        APatientDto newPatientData = (APatientDto) parameters[2];

        Optional<Patient> optPatient = patientService.getPatient(patientId);

        if (optPatient.isEmpty()){
            buildResponse(context, "{patient.invalid}");
            return false;
        }
        else{
            Patient patient = optPatient.get();
            return patientUpdateIsAllowed(patient, institutionId, newPatientData) && auditablePatientDataIsComplete(context, newPatientData);
        }
    }

    private boolean patientUpdateIsAllowed(Patient patient, Integer institutionId,APatientDto newPatientData){
        if (allPatientDataCanBeUpdated(patient, institutionId))
            return true;
        else {
            BasicDataPersonDto actualPatientData = personExternalService.getBasicDataPerson(patient.getPersonId());
            return restrictedFieldsAreNotUpdated(actualPatientData, newPatientData);
        }
    }

    private boolean allPatientDataCanBeUpdated(Patient patient, Integer institutionId){
		List<UserRoleDto> auditorRole = hospitalUserRoleMapper.toListUserRoleDto(getRolesByUser.execute(UserInfo.getCurrentAuditor(), institutionId))
				.stream().filter(a -> a.getRoleId() == ERole.AUDITOR_MPI.getId()).collect(Collectors.toList());

        Short patientType = patient.getTypeId();
        return (!patientType.equals(PatientType.VALIDATED) &&
                !patientType.equals(PatientType.PERMANENT) &&
                !patientType.equals(PatientType.PERMANENT_NOT_VALIDATED)) || this.featureFlagsService.isOn(AppFeature.HABILITAR_EDITAR_PACIENTE_COMPLETO)
				|| !auditorRole.isEmpty();
    }

    private boolean restrictedFieldsAreNotUpdated(BasicDataPersonDto actualPatientData, APatientDto newPatientData){
        return namesAreNotUpdated(actualPatientData, newPatientData) &&
                identificationIsNotUpdated(actualPatientData, newPatientData) &&
                genderIsNotUpdated(actualPatientData, newPatientData) &&
                birthDateIsNotUpdated(actualPatientData, newPatientData);
    }

    private boolean namesAreNotUpdated(BasicDataPersonDto actualPatientData, APatientDto newPatientData){
        return areEqual(actualPatientData.getFirstName(), newPatientData.getFirstName()) &&
                areEqual(actualPatientData.getMiddleNames(), newPatientData.getMiddleNames()) &&
                areEqual(actualPatientData.getLastName(), newPatientData.getLastName()) &&
                areEqual(actualPatientData.getOtherLastNames(), newPatientData.getOtherLastNames());
    }

    private boolean identificationIsNotUpdated(BasicDataPersonDto actualPatientData, APatientDto newPatientData){
        return areEqual(actualPatientData.getIdentificationNumber(), newPatientData.getIdentificationNumber()) &&
                areEqual(actualPatientData.getIdentificationTypeId(), newPatientData.getIdentificationTypeId());
    }

    private boolean genderIsNotUpdated(BasicDataPersonDto actualPatientData, APatientDto newPatientData){
        return areEqual(actualPatientData.getGender().getId(), newPatientData.getGenderId());
    }

    private boolean birthDateIsNotUpdated(BasicDataPersonDto actualPatientData, APatientDto newPatientData){
        LocalDate newBirthDate = null;
        if (newPatientData.getBirthDate() != null)
            newBirthDate = newPatientData.getBirthDate().toLocalDate();
        return areEqual(actualPatientData.getBirthDate(), newBirthDate);
    }

    private boolean areEqual(Object o1, Object o2){
        if (o1 == null && o2 == null)
            return true;
        else if (o1 == null || o2 == null)
            return false;
        else
            return o1.equals(o2);
    }

    private void buildResponse(ConstraintValidatorContext context, String message) {
        context.disableDefaultConstraintViolation();
        context.buildConstraintViolationWithTemplate(message)
                .addConstraintViolation();
    }

    private boolean auditablePatientDataIsComplete(ConstraintValidatorContext context, APatientDto patientDto) {
		boolean auditablePatientDataIsComplete = patientDto.getAuditType() == null || (patientDto.getAuditType().equals(EAuditType.UNAUDITED) || patientDto.getAuditType().equals(EAuditType.AUDITED) || (patientDto.getAuditType().equals(EAuditType.TO_AUDIT) && patientDto.getMessage() != null));
    	if (!auditablePatientDataIsComplete)
			buildResponse(context, "{patient.auditable.message.null}");
    	return auditablePatientDataIsComplete;
	}
}
