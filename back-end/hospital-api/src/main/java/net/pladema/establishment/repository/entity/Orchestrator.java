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
}
