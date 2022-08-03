package ar.lamansys.sgh.clinichistory.domain.hce.summary;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class HealthcareProfessionalBo {

    private Integer id;

    private CHPersonBo person;

    private String licenceNumber;

    public HealthcareProfessionalBo(Integer id, String licenceNumber,
                                    Integer personId, String firstName,
                                    String lastName, String identificationNumber, String nameSelfDetermination) {
        this.person = new CHPersonBo(personId, firstName, lastName, identificationNumber, nameSelfDetermination);
        this.id = id;
        this.licenceNumber = licenceNumber;
    }
}
