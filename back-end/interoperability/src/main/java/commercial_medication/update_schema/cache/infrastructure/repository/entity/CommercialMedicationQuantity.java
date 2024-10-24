package commercial_medication.update_schema.cache.infrastructure.repository.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import commercial_medication.update_schema.cache.domain.CommercialMedicationMasterData;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "quantity", schema = "commercial_medication")
@Entity
public class CommercialMedicationQuantity implements Serializable {

	private static final long serialVersionUID = 8592337900389690437L;

	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "description", length = 50)
	private String description;

	public CommercialMedicationQuantity(CommercialMedicationMasterData quantity) {
		this.id = quantity.getId();
		this.description = quantity.getDescription();
	}
}
