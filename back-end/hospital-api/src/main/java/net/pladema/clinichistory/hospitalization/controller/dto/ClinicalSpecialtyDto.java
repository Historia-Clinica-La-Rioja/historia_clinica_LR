package net.pladema.clinichistory.hospitalization.controller.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ClinicalSpecialtyDto implements Serializable{

    /**
	 * 
	 */
	private static final long serialVersionUID = -806572710505828007L;

	private Integer id;

    private String name;
}
