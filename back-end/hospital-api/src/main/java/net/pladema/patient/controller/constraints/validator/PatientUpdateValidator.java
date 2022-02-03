package net.pladema.patient.controller.constraints.validator;

import ar.lamansys.sgh.shared.infrastructure.input.service.BasicDataPersonDto;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import net.pladema.patient.controller.constraints.PatientUpdateValid;
import net.pladema.patient.controller.dto.APatientDto;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.patient.repository.entity.PatientType;
import net.pladema.patient.service.PatientService;
import net.pladema.person.controller.service.PersonExternalService;
import ar.lamansys.sgx.shared.featureflags.AppFeature;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.constraintvalidation.SupportedValidationTarget;
import javax.validation.constraintvalidation.ValidationTarget;
import java.time.LocalDate;
import java.util.Optional;

@SupportedValidationTarget(ValidationTarget.PARAMETERS)
public class PatientUpdateValidator implements ConstraintValidator<PatientUpdateValid, Object[]> {

    private final PatientService patientService;

    private final PersonExternalService personExternalService;

    private final FeatureFlagsService featureFlagsService;

    public PatientUpdateValidator(PatientService patientService,
                                  PersonExternalService personExternalService,
                                  FeatureFlagsService featureFlagsService){
        this.patientService = patientService;
        this.personExternalService = personExternalService;
        this.featureFlagsService = featureFlagsService;
    }

    @Override
    public void initialize(PatientUpdateValid constraintAnnotation) {
        // Nothing to do
    }

    @Override
    public boolean isValid(Object[] parameters, ConstraintValidatorContext context) {

        Integer patientId = (Integer) parameters[0];
        APatientDto newPatientData = (APatientDto) parameters[2];

        Optional<Patient> optPatient = patientService.getPatient(patientId);

        if (optPatient.isEmpty()){
            buildResponse(context, "{patient.invalid}");
            return false;
        }
        else{
            Patient patient = optPatient.get();
            return patientUpdateIsAllowed(patient, newPatientData);
        }
    }

    private boolean patientUpdateIsAllowed(Patient patient, APatientDto newPatientData){
        if (allPatientDataCanBeUpdated(patient))
            return true;
        else {
            BasicDataPersonDto actualPatientData = personExternalService.getBasicDataPerson(patient.getPersonId());
            return restrictedFieldsAreNotUpdated(actualPatientData, newPatientData);
        }
    }

    private boolean allPatientDataCanBeUpdated(Patient patient){
        Short patientType = patient.getTypeId();
        return (!patientType.equals(PatientType.VALIDATED) &&
                !patientType.equals(PatientType.PERMANENT) &&
                !patientType.equals(PatientType.PERMANENT_NOT_VALIDATED)) || this.featureFlagsService.isOn(AppFeature.HABILITAR_EDITAR_PACIENTE_COMPLETO);
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
}
