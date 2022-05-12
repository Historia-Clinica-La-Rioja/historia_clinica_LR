package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.hce.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class HCEBasicPersonDataDto implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 6477968773546760323L;

    private Integer id;

	private String firstName;

    private String lastName;

    private String birthDate;

    private String identificationNumber;

	private String nameSelfDetermination;

}
