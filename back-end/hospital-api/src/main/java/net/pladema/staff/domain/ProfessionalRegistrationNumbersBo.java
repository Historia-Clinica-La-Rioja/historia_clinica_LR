package net.pladema.staff.domain;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ProfessionalRegistrationNumbersBo {

    private Integer healthcareProfessionalId;

    private String firstName;

    private String lastName;

    private String nameSelfDetermination;

    private List<ProfessionalLicenseNumberBo> license;
}
