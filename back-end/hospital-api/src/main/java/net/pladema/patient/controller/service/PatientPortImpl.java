package net.pladema.patient.controller.service;

import ar.lamansys.sgh.shared.infrastructure.input.service.BasicDataPersonDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import net.pladema.patient.controller.dto.PatientMedicalCoverageDto;
import net.pladema.patient.controller.mapper.PatientMedicalCoverageMapper;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.patient.service.PatientMedicalCoverageService;
import net.pladema.patient.service.PatientService;
import net.pladema.patient.service.domain.PatientMedicalCoverageBo;
import net.pladema.person.controller.service.PersonExternalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PatientPortImpl implements PatientExternalService {

    private static final Logger LOG = LoggerFactory.getLogger(PatientPortImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final PatientService patientService;

    private final PatientMedicalCoverageService patientMedicalCoverageService;

    private final PersonExternalService personExternalService;

    private final PatientMedicalCoverageMapper patientMedicalCoverageMapper;

    public PatientPortImpl(PatientService patientService, PatientMedicalCoverageService patientMedicalCoverageService, PersonExternalService personExternalService,
                           PatientMedicalCoverageMapper patientMedicalCoverageMapper) {
        this.patientService = patientService;
        this.patientMedicalCoverageService = patientMedicalCoverageService;
        this.personExternalService = personExternalService;
        this.patientMedicalCoverageMapper = patientMedicalCoverageMapper;
    }

    @Override
    public BasicPatientDto getBasicDataFromPatient(Integer patientId) {
        LOG.debug("Input parameters -> patientId {}", patientId);
        Patient patient = patientService.getPatient(patientId)
                .orElseThrow(() -> new EntityNotFoundException("patient.invalid"));
        BasicDataPersonDto personData = personExternalService.getBasicDataPerson(patient.getPersonId());
        BasicPatientDto result = new BasicPatientDto(patient.getId(), personData,patient.getTypeId());
        LOG.debug(OUTPUT, result);
        return result;
    }

    @Override
    public Map<Integer, BasicPatientDto> getBasicDataFromPatientsId(Set<Integer> patientIds) {
        LOG.debug("Input parameters -> patientIds {}", patientIds.size());
        LOG.trace("Input parameters -> patientIds {}", patientIds);
        Map<Integer, Patient> patientsMap = patientService.getPatients(patientIds).parallelStream()
                                .collect(Collectors.toMap(Patient::getId, patient -> patient));

        Set<Integer> personIds = patientsMap.values().parallelStream()
                                    .map(Patient::getPersonId)
                                    .collect(Collectors.toSet());

        Map<Integer, BasicDataPersonDto> personDatas = personExternalService.getBasicDataPerson(personIds).parallelStream()
                                                .collect(Collectors.toMap(BasicDataPersonDto::getId, person -> person));


        Map<Integer, BasicPatientDto> result = patientsMap.entrySet().parallelStream()
                .collect(Collectors.toMap(
                        p -> p.getKey(),
                        p -> new BasicPatientDto(p.getKey(), personDatas.get(p.getValue().getPersonId()), p.getValue().getTypeId())
                ));
        LOG.debug("Result size {}", result.size());
        LOG.trace(OUTPUT, result);
        return result;
    }

    @Override
    public PatientMedicalCoverageDto getCoverage(Integer patientMedicalCoverageId) {
        LOG.debug("Input parameter -> patientMedicalCoverageId {}", patientMedicalCoverageId);
        PatientMedicalCoverageBo queryResult = patientMedicalCoverageService.getCoverage(patientMedicalCoverageId).orElse(new PatientMedicalCoverageBo());
        PatientMedicalCoverageDto result = patientMedicalCoverageMapper.toPatientMedicalCoverageDto(queryResult);
        LOG.debug(OUTPUT, result);
        return result;
    }

}
