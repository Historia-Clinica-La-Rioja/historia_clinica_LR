package net.pladema.internation.service.internment.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class RoomBo {

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
