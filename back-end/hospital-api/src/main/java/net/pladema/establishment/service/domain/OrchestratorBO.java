package net.pladema.establishment.service.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class OrchestratorBO {

	private Integer id;

	private String name;

	private String baseTopic;

	private Integer sectorId;
}
