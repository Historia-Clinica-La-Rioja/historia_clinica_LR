package net.pladema.establishment.controller.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RoomDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = -6806500543924261426L;

	private Integer id;
	
	private String description;
	
	private String type;

    private String roomNumber;

    private SectorDto sector;

}
