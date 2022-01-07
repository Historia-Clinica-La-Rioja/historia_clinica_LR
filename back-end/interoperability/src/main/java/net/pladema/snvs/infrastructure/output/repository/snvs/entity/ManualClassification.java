package net.pladema.snvs.infrastructure.output.repository.snvs.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "manual_classification")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ManualClassification {

	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "description", length = 255, nullable = false)
	private String description;
}
