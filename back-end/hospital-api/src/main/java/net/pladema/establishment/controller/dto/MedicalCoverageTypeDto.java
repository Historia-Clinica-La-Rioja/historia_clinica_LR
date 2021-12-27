package net.pladema.establishment.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class MedicalCoverageTypeDto implements Serializable {

    /**
	 *
	 */
	private static final long serialVersionUID = 3940040590318704813L;

	private Short id;

    private String value;

}
