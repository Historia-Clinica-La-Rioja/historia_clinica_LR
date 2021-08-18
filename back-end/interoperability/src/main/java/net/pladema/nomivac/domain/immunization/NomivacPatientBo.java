package net.pladema.nomivac.domain.immunization;

import lombok.Getter;

@Getter
public class NomivacPatientBo {

    private final Integer id;

    private final String firstName;

    private final String middleNames;

    private final String lastName;

    private final String identificationNumber;

    public NomivacPatientBo(Integer id, String firstName, String middleNames, String lastName, String identificationNumber) {
        this.id = id;
        this.firstName = firstName;
        this.middleNames = middleNames;
        this.lastName = lastName;
        this.identificationNumber = identificationNumber;
    }

    public String getCompleteName() {
        StringBuilder result = new StringBuilder();
        if (firstName != null)
            result.append(firstName);
        if (middleNames != null)
            result.append(" " + middleNames);
        if (lastName != null)
            result.append(" " + lastName);
        return result.toString().trim();
    }
}
