package net.pladema.staff.repository.mocks;

import net.pladema.person.repository.entity.Person;
import net.pladema.staff.repository.entity.HealthcareProfessional;

public class StaffTestMocks {

    public static Person createMinimumPerson(String firstName, String lastName, String identificationNumber) {
        Person result = new Person();
        result.setFirstName(firstName);
        result.setLastName(lastName);
        result.setIdentificationNumber(identificationNumber);
        return result;
    }

    public static HealthcareProfessional createMinimumProfessional(Integer personId, String licenceNumber) {
        HealthcareProfessional result = new HealthcareProfessional();
        result.setPersonId(personId);
        result.setLicenseNumber(licenceNumber);
        return result;
    }
}
