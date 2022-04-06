package net.pladema.staff.repository.domain;

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

    private Integer personId;

    public HealthcareProfessionalVo(Integer id, String licenceNumber, String firstName,
                                    String lastName, String identificationNumber,Integer personId, String nameSelfDetermination){
        super(firstName, lastName, identificationNumber, nameSelfDetermination);
        this.id = id;
        this.licenceNumber = licenceNumber;
        this.personId = personId;
    }
}
