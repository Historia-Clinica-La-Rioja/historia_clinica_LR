package net.pladema.establishment.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SectorDto {

	private Integer id;

    private String description;

	private Boolean hasDoctorsOffice;

	private Short type;
}
