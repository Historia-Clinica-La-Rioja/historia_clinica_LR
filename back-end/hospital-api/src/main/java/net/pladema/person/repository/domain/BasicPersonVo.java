package net.pladema.person.repository.domain;

import java.io.Serializable;

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
public class BasicPersonVo implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -7242766364760347808L;

	private String firstName;

    private String lastName;
    
    private String identificationType;
    
    private String identificationNumber;
}
