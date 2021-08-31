package ar.lamansys.sgh.shared.infrastructure.input.service;

import lombok.*;

import java.io.Serializable;

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
