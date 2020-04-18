package net.pladema.internation.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class RoomDto implements Serializable {

    private Integer id;

    private Short roomNumber;

    private SectorDto sector;

    private ClinicalSpecialityDto speciality;

}
