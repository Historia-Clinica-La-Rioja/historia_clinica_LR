package commercialmedication.cache.infrastructure.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import commercialmedication.cache.domain.CommercialMedicationMasterData;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "size", schema = "commercial_medication")
@Entity
public class CommercialMedicationSize implements Serializable {

	private static final long serialVersionUID = -2499150375981155017L;
	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "description", length = 50, nullable = false)
	private String description;

	public CommercialMedicationSize(CommercialMedicationMasterData size) {
		this.id = size.getId();
		this.description = size.getDescription();
	}
}
