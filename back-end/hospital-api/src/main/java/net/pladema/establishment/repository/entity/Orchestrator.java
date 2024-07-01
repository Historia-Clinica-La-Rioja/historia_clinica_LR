package net.pladema.establishment.repository.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Time;

@Entity
@Table(name = "orchestrator")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class Orchestrator {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name", nullable = false, length = 40)
	private String name;

	@Column(name = "base_topic", nullable = false, length = 250)
	private String baseTopic;

	@Column(name = "sector_id", nullable = false)
	private Integer sectorId;

	@Column(name = "attemps_number", nullable = false)
	private Integer attempsNumber ;

	@Column(name = "execution_start_time", nullable = false)
	private Time executionStartTime ;

	@Column(name = "execution_end_time", nullable = false)
	private Time executionEndTime;

	@Column(name = "weight_days", nullable = false)
	private Double weightDays;

	@Column(name = "weight_size", nullable = false)
	private Double weightSize ;

	@Column(name = "weight_priority", nullable = false)
	private Double weightPriority ;

	@Column(name = "number_to_move", nullable = false)
	private Integer numberToMove ;

	@Column(name = "massive_retry", nullable = false)
	private Boolean massiveRetry;


}
