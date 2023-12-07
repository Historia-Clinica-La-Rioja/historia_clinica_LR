package net.pladema.establishment.service.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class EquipmentBO {

	private Integer id;

	private String name;

	private String aeTitle;

	private Integer sectorId;

	private Integer orchestratorId;

	private Integer pacServerId;

	private Integer modalityId;

	private Boolean createId;
}
