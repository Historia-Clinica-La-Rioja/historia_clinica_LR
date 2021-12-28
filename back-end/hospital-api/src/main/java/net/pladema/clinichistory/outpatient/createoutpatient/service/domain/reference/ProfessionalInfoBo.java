package net.pladema.clinichistory.outpatient.createoutpatient.service.domain.reference;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProfessionalInfoBo {

    private Integer id;

    private String firstName;

    private String lastName;

}
