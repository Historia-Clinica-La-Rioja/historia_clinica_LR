package net.pladema.patient.controller.service;

import net.pladema.patient.controller.dto.BasicPatientDto;
import net.pladema.patient.repository.entity.Patient;
import net.pladema.patient.service.PatientService;
import net.pladema.person.controller.dto.BasicDataPersonDto;
import net.pladema.person.controller.service.PersonExternalService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class PatientExternalServiceImpl implements PatientExternalService {

    private static final Logger LOG = LoggerFactory.getLogger(PatientExternalServiceImpl.class);

    public static final String OUTPUT = "Output -> {}";

    private final PatientService patientService;

    private final PersonExternalService personExternalService;

    public PatientExternalServiceImpl(PatientService patientService, PersonExternalService personExternalService) {
        this.patientService = patientService;
        this.personExternalService = personExternalService;
    }

    @Override
    public BasicPatientDto getBasicDataFromPatient(Integer patientId) {
        LOG.debug("Input parameters -> patientId {}", patientId);
        Patient patient = patientService.getPatient(patientId)
                .orElseThrow(() -> new EntityNotFoundException("patient.invalid"));
        BasicDataPersonDto personData = personExternalService.getBasicDataPerson(patient.getPersonId());
        BasicPatientDto result = new BasicPatientDto(patient.getId(), personData);
        LOG.debug(OUTPUT, result);
        return result;
    }

}
