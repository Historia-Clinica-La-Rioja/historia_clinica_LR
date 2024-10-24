package commercial_medication.update_schema.cache.infrastructure.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import commercial_medication.update_schema.cache.domain.CommercialMedicationMasterData;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "form", schema = "commercial_medication")
@Entity
public class CommercialMedicationForm implements Serializable {

	private static final long serialVersionUID = 8592337900389690437L;

	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "description", length = 50)
	private String description;

	public CommercialMedicationForm(CommercialMedicationMasterData form) {
		this.id = form.getId();
		this.description = form.getDescription();
	}
}
