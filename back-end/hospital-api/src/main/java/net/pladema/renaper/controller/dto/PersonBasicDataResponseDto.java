package net.pladema.renaper.controller.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PersonBasicDataResponseDto implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = 6477968773546760323L;

	private String firstName;

    private String lastName;

    private String birthDate;
}
