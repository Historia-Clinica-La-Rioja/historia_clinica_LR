package net.pladema.clinichistory.hospitalization.service.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.establishment.repository.domain.RoomVo;

import java.io.Serializable;

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

	public RoomBo(RoomVo room) {
		this.id = room.getId();
		this.description = room.getDescription();
		this.type = room.getType();
		this.roomNumber = room.getRoomNumber();
	}

    public RoomBo(Integer id, SectorBo sector, String description) {
        this.id = id;
        this.sector = sector;
        this.description = description;
    }
}
