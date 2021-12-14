package net.pladema.snvs.infrastructure.output.repository.patient;

import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPatientPort;
import net.pladema.snvs.application.ports.patient.PatientStorage;
import net.pladema.snvs.domain.patient.PatientDataBo;
import net.pladema.snvs.domain.patient.PersonDataBo;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PatientStorageImpl implements PatientStorage {

    private final SharedPatientPort sharedPatientPort;

    public PatientStorageImpl(SharedPatientPort sharedPatientPort) {
        this.sharedPatientPort = sharedPatientPort;
    }


    @Override
    public Optional<PatientDataBo> getPatientInfo(Integer patientId) {
        return Optional.of(sharedPatientPort.getBasicDataFromPatient(patientId))
                .map(this::mapData);
    }

    private PatientDataBo mapData(BasicPatientDto basicPatientDto) {
        return new PatientDataBo(basicPatientDto.getId(), mapPatientData(basicPatientDto));
    }

    private PersonDataBo mapPatientData(BasicPatientDto basicPatientDto) {
        return new PersonDataBo(basicPatientDto.getFirstName(), basicPatientDto.getLastName(),
                basicPatientDto.getPerson().getIdentificationTypeId(), basicPatientDto.getIdentificationNumber(),
                null,
                basicPatientDto.getPerson().getBirthDate(),
                basicPatientDto.getPerson().getGender().getId(),//"M",
                "011-4224-0099",
                "mail_mail@dominio.com",
                null);
    }
}
