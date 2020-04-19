package net.pladema.person.controller.service;

import net.pladema.patient.controller.dto.APatientDto;
import net.pladema.person.controller.dto.BMPersonDto;
import net.pladema.person.controller.dto.BasicDataPersonDto;

import java.util.List;

public interface PersonExternalService {

    BMPersonDto addPerson(APatientDto patient);

    void addPersonExtended(APatientDto patient, Integer PersonId, Integer AddressId);

    List<Integer> getPersonByDniAndGender(Short identificationTypeId, String identificationNumber, Short genderId);

    BasicDataPersonDto getBasicDataPerson(Integer personId);
}
