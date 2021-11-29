package net.pladema.snvs.infrastructure.output.repository.snvs.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "snvs_group")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SnvsGroup {

	@Id
	@Column(name = "group_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer groupId;

	@Column(name = "event_id", nullable = false)
	private Integer eventId;

	@Column(name = "group_event_id", nullable = false)
	private Integer groupEventId;

	@Column(name = "environment", nullable = false)
	private Integer environment;

	@Column(name = "manual_classification_id", nullable = false)
	private Integer manualClassificationId;
}
