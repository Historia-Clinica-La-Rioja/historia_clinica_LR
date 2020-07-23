package net.pladema.staff.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class HealthcareProfessionalVo extends BasicPersonalDataVo {

    private Integer id;

    private String licenceNumber;


    public HealthcareProfessionalVo(Integer id, String licenceNumber, String firstName,
                                    String lastName, String identificationNumber){
        super(firstName, lastName, identificationNumber);
        this.id = id;
        this.licenceNumber = licenceNumber;
    }
}
