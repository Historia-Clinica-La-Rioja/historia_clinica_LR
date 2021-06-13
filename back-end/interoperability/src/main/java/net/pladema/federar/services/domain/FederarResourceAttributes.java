package net.pladema.federar.services.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class FederarResourceAttributes {

    private Integer id;
    private String identificationNumber;
    private String firstName;
    private String middleNames;
    private String lastName;
    private String otherLastNames;
    private Short genderId;
    private LocalDate birthDate;

    public String getPatientId(){
        return id != null ? id.toString() : null;
    }
}
