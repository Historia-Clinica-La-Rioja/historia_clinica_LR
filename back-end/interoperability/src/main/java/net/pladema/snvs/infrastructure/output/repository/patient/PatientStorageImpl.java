package net.pladema.snvs.infrastructure.output.repository.patient;

import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPatientPort;
import net.pladema.snvs.application.ports.patient.PatientStorage;
import net.pladema.snvs.domain.patient.AddressDataBo;
import net.pladema.snvs.domain.patient.PatientDataBo;
import net.pladema.snvs.domain.patient.PersonDataBo;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
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
        return new PatientDataBo(basicPatientDto.getId(), mockData());
    }

    private PersonDataBo mapPatientData(BasicPatientDto basicPatientDto) {
        return new PersonDataBo(basicPatientDto.getFirstName(), basicPatientDto.getLastName(),
                basicPatientDto.getPerson().getIdentificationTypeId(), basicPatientDto.getIdentificationNumber(),
                null,
                basicPatientDto.getPerson().getBirthDate(),
                basicPatientDto.getPerson().getGender().getId(),//"M",
                "011-4224-0099",
                "mail_mail@dominio.com",
                new AddressDataBo("Calle número 8000", 4, 6007010, (short)2, 200));
    }

    private PersonDataBo mockData() {
        return new PersonDataBo("Test1", "Prueba",
                (short) 1, "34000001", null,
                LocalDate.of(1990,10,10),
                (short) 1,//"M",
                "011-4224-0099",
                "mail_mail@dominio.com",
                new AddressDataBo("Calle número 8000", 4, 6007010, (short)2, 200));
    }
}
