package net.pladema.staff.service.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class BasicPersonalDataBo {

    private String firstName;

    private String lastName;

    private String identificationNumber;

	private String nameSelfDetermination;
}
