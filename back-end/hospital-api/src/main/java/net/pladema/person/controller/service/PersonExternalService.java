package net.pladema.person.controller.service;

import java.util.List;

import net.pladema.patient.controller.dto.APatientDto;
import net.pladema.person.controller.dto.BMPersonDto;
import net.pladema.person.controller.dto.BasicDataPersonDto;
import net.pladema.person.repository.entity.PersonExtended;

public interface PersonExternalService {

    BMPersonDto addPerson(APatientDto patient);
    
    BMPersonDto updatePerson(APatientDto patient, Integer personId);

    void addPersonExtended(APatientDto patient, Integer personId, Integer addressId);

    PersonExtended updatePersonExtended(APatientDto patient, Integer personId);
    
    List<Integer> getPersonByDniAndGender(Short identificationTypeId, String identificationNumber, Short genderId);

    BasicDataPersonDto getBasicDataPerson(Integer personId);

}
