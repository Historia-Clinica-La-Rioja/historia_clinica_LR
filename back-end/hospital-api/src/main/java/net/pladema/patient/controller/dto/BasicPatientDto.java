package net.pladema.patient.controller.dto;

import java.io.Serializable;

import lombok.*;
import net.pladema.person.controller.dto.BasicDataPersonDto;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class BasicPatientDto implements Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = 2121769190622994359L;

	private Integer id;

    private BasicDataPersonDto person;

    private Short typeId;

}
