package net.pladema.patient.infrastructure.input.shared;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedPatientPort;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicDataPersonDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.BasicPatientDto;
import net.pladema.patient.controller.service.PatientExternalService;
import ar.lamansys.sgh.shared.infrastructure.input.service.GenderDto;
import org.springframework.stereotype.Service;

@Service
public class SharedPatientImpl implements SharedPatientPort {

    private final PatientExternalService patientExternalService;

    public SharedPatientImpl(PatientExternalService patientExternalService) {
        this.patientExternalService = patientExternalService;
    }


    @Override
    public BasicPatientDto getBasicDataFromPatient(Integer patientId) {
        var result = patientExternalService.getBasicDataFromPatient(patientId);
        return new BasicPatientDto(result.getId(), mapPersonData(result.getPerson()), result.getTypeId());
    }

    private BasicDataPersonDto mapPersonData(net.pladema.person.controller.dto.BasicDataPersonDto person) {
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

    private GenderDto mapGender(net.pladema.person.controller.dto.GenderDto gender) {
        var result = new GenderDto();
        result.setId(gender.getId());
        result.setDescription(gender.getDescription());
        return result;
    }

}
