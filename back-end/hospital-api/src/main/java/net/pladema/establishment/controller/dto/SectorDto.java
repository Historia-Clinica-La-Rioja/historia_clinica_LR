package net.pladema.establishment.controller.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SectorDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1255959625805084612L;

	private Integer id;

    private String description;
}
