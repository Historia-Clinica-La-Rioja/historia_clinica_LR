package ar.lamansys.sgh.shared.infrastructure.input.service;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProfessionalPersonDto implements Serializable {

    private static final long serialVersionUID = 1781479099977648184L;

    private Integer id;

    private String firstName;

    private String lastName;

    private String nameSelfDetermination;

	private String middleNames;

	private String otherLastNames;

	private String fullName;

	public ProfessionalPersonDto(Integer id, String firstName, String lastName, String nameSelfDetermination) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.nameSelfDetermination = nameSelfDetermination;
	}
}