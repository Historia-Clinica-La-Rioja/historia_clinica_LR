package net.pladema.patient.infrastructure.input.shared;

import ar.lamansys.sgh.shared.infrastructure.input.service.*;
import net.pladema.audit.service.domain.enums.EActionType;
import net.pladema.patient.controller.dto.APatientDto;
import net.pladema.patient.controller.mapper.PatientMapper;
import net.pladema.patient.controller.service.PatientExternalService;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.patient.service.PatientMedicalCoverageService;
import net.pladema.patient.service.PatientService;
import net.pladema.patient.service.domain.HealthInsuranceBo;
import net.pladema.patient.service.domain.PatientMedicalCoverageBo;
import net.pladema.patient.service.domain.PrivateHealthInsuranceBo;
import net.pladema.person.controller.dto.BMPersonDto;
import net.pladema.person.controller.service.PersonExternalService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Service
public class SharedPatientImpl implements SharedPatientPort {

    private final PatientExternalService patientExternalService;

    private final PersonExternalService personExternalService;

    private final PatientService patientService;

    private final PatientMapper patientMapper;

    private final PatientMedicalCoverageService patientMedicalCoverageService;

    private final Short TYPE_ID_PERMANENTE_NO_VALIDADO = 7;


    public SharedPatientImpl(PatientExternalService patientExternalService,
                             PersonExternalService personExternalService,
                             PatientService patientService,
                             PatientMapper patientMapper,
                             PatientMedicalCoverageService patientMedicalCoverageService) {
        this.patientExternalService = patientExternalService;
        this.personExternalService = personExternalService;
        this.patientService = patientService;
        this.patientMapper = patientMapper;
        this.patientMedicalCoverageService = patientMedicalCoverageService;
    }


    @Override
    public BasicPatientDto getBasicDataFromPatient(Integer patientId) {
        var result = patientExternalService.getBasicDataFromPatient(patientId);
        return new BasicPatientDto(result.getId(), mapPersonData(result.getPerson()), result.getTypeId());
    }

    @Override
    public List<Integer> getPatientId(Short identificationTypeId, String identificationNumber, Short genderId) {
        return personExternalService.getPersonByDniAndGender(identificationTypeId, identificationNumber, genderId);
    }

    @Override
    public Integer createPatient(RequiredPatientDataDto requiredPatientDataDto) {
        APatientDto patientDto = mapToAPatientDto(requiredPatientDataDto);
        BMPersonDto createdPerson = personExternalService.addPerson(patientDto);
        personExternalService.addPersonExtended(patientDto, createdPerson.getId(), null);
        Patient createdPatient = persistPatientData(patientDto, createdPerson, patient -> {
        });
        patientService.auditActionPatient(requiredPatientDataDto.getInstitutionId(), createdPatient.getId(), EActionType.CREATE);
        return createdPatient.getId();
    }

    @Override
    public void saveMedicalCoverages(List<ExternalPatientCoverageDto> externalPatientCoverages, Integer patientId) {
        externalPatientCoverages.stream().map(this::mapToPatientMedicalCoverage);
        patientMedicalCoverageService.saveExternalCoverages(externalPatientCoverages.stream().map(this::mapToPatientMedicalCoverage).collect(Collectors.toList()), patientId);
    }

    private PatientMedicalCoverageBo mapToPatientMedicalCoverage(ExternalPatientCoverageDto externalPatientCoverageDto) {
        PatientMedicalCoverageBo pmc = new PatientMedicalCoverageBo();
        pmc.setVigencyDate(externalPatientCoverageDto.getVigencyDate());
        pmc.setAffiliateNumber(externalPatientCoverageDto.getAffiliateNumber());
        pmc.setActive(externalPatientCoverageDto.getActive());
        ExternalCoverageDto ecDto = externalPatientCoverageDto.getMedicalCoverage();
        if (ecDto.getType().equals(EMedicalCoverageTypeDto.PREPAGA))
            pmc.setMedicalCoverage(new PrivateHealthInsuranceBo(ecDto.getId(), ecDto.getName(), ecDto.getCuit(), ecDto.getType().getId()));
        else
            pmc.setMedicalCoverage(new HealthInsuranceBo(ecDto.getId(),ecDto.getName(), ecDto.getCuit(), null,null,ecDto.getType().getId()));
        return pmc;
    }

    private Patient persistPatientData(APatientDto patientDto, BMPersonDto createdPerson, Consumer<Patient> addIds) {
        Patient patientToAdd = patientMapper.fromPatientDto(patientDto);
        patientToAdd.setPersonId(createdPerson.getId());
        addIds.accept(patientToAdd);
        return patientService.addPatient(patientToAdd);
    }

    private APatientDto mapToAPatientDto(RequiredPatientDataDto requiredPatientDataDto) {
        APatientDto aPatientDto = new APatientDto();
        aPatientDto.setTypeId(TYPE_ID_PERMANENTE_NO_VALIDADO);
        aPatientDto.setBirthDate(requiredPatientDataDto.getBirthDate());
        aPatientDto.setFirstName(requiredPatientDataDto.getFirstName());
        aPatientDto.setLastName(requiredPatientDataDto.getLastName());
        aPatientDto.setGenderId(requiredPatientDataDto.getGenderId());
        aPatientDto.setIdentificationTypeId(requiredPatientDataDto.getIdentificationTypeId());
        aPatientDto.setIdentificationNumber(requiredPatientDataDto.getIdentificationNumber());
        aPatientDto.setPhoneNumber(requiredPatientDataDto.getPhoneNumber());
        aPatientDto.setEmail(requiredPatientDataDto.getEmail());
        return aPatientDto;
    }

    private BasicDataPersonDto mapPersonData(BasicDataPersonDto person) {
        var result = new BasicDataPersonDto();
        result.setId(person.getId());
        result.setFirstName(person.getFirstName());
        result.setMiddleNames(person.getMiddleNames());
        result.setLastName(person.getLastName());
        result.setOtherLastNames(person.getOtherLastNames());
        result.setIdentificationTypeId(person.getIdentificationTypeId());
        result.setIdentificationType(person.getIdentificationType());
        result.setIdentificationNumber(person.getIdentificationNumber());
        result.setGender(mapGender(person.getGender()));
        result.setAge(person.getAge());
        result.setBirthDate(person.getBirthDate());
        return result;
    }

    private GenderDto mapGender(GenderDto gender) {
        var result = new GenderDto();
        result.setId(gender.getId());
        result.setDescription(gender.getDescription());
        return result;
    }

}
