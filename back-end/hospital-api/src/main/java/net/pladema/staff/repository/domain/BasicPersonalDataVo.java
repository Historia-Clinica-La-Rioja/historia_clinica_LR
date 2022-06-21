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
@AllArgsConstructor
public class BasicPersonalDataVo {

    private String firstName;

    private String lastName;

    private String identificationNumber;

	private String nameSelfDetermination;

}
