package net.pladema.establishment.repository.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "equipment")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor

public class Equipment {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name", nullable = false, length = 40)
	private String name;

	@Column(name = "ae_title", nullable = false, length = 15)
	private String aeTitle;

	@Column(name = "sector_id", nullable = false)
	private Integer sectorId;

	@Column(name = "orchestrator_id", nullable = false)
	private Integer orchestratorId;

	@Column(name = "pac_server_id", nullable = false)
	private Integer pacServerId;

	@Column(name = "modality_id", nullable = false)
	private Integer modalityId;

}
