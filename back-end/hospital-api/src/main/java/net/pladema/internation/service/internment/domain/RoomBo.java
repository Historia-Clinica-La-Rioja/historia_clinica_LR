package net.pladema.internation.service.internment.domain;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RoomBo implements Serializable {

	private static final long serialVersionUID = -5646044578379828761L;

	private Integer id;

    private String description;

    private String type;

    private String roomNumber;

    private SectorBo sector;

    public RoomBo(Integer id, String roomNumber, SectorBo sector) {
        this.id = id;
        this.roomNumber = roomNumber;
        this.sector = sector;
    }
}
