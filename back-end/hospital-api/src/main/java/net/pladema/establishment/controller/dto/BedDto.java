package net.pladema.establishment.controller.dto;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.establishment.infrastructure.input.rest.dto.FetchAttentionPlaceStatusDto;

@Getter
@Setter
@ToString
public class BedDto implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 3940040590318704813L;

	private Integer id;

    private String bedNumber;

    private RoomDto room;
    
    private Boolean free;

    private Boolean isBlocked;

}
