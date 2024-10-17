package net.pladema.medication.infrastructure.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CommercialMedicationArticleDrugPK implements Serializable {

	private static final long serialVersionUID = -3850909568046934195L;

	@Column(name = "article_id")
	private Integer articleId;

	@Column(name = "new_drug_id")
	private Integer newDrugId;

}
