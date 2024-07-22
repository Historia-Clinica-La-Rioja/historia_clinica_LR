package net.pladema.establishment.service.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


import java.sql.Time;

@Getter
@Setter
@ToString
public class OrchestratorBO {

	private Integer id;

	private String name;

	private String baseTopic;

	private Integer sectorId;

	private Integer attempsNumber;

	private Time executionStartTime;

	private Time executionEndTime;

	private Double weightDays;

	private Double weightSize;

	private Double weightPriority;

	private Integer numberToMove;

	private Boolean findStudies;
}
