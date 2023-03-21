package ar.lamansys.sgx.shared.actuator.infrastructure.output.repository;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "system_property")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class SystemProperty {

	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "property", nullable = false)
	private String property;

	@Column(name = "node_id", nullable = false)
	private String nodeId;

	@Column(name = "value", columnDefinition = "TEXT")
	private String value;

	@Column(name = "description")
	private String description;

	@Column(name = "origin")
	private String origin;

	@Column(name = "updated_on")
	private LocalDateTime updatedOn;

	public SystemProperty(Integer id, String property, String value, String description, String origin, String nodeId, LocalDateTime updatedOn) {
		this.id = id;
		this.property = property;
		this.nodeId = nodeId;
		this.description = description;
		this.value = value;
		this.origin = origin;
		this.updatedOn = updatedOn;
	}

	public SystemProperty(String property, String value, String description, String origin, String nodeId, LocalDateTime updatedOn) {
		this(null, property, value, description, origin, nodeId, updatedOn);
	}



}
