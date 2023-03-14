package net.pladema.establishment.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;

@Getter
@Setter
@ToString
public class EquipmentDto implements Serializable {

	private Integer id;

    private String name;

	private Integer modalityId;
}
