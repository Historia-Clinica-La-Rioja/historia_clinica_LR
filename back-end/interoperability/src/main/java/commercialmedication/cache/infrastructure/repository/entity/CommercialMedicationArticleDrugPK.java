package commercialmedication.cache.infrastructure.repository.entity;

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

	@Column(name = "article_id", nullable = false)
	private Integer articleId;

	@Column(name = "new_drug_id", nullable = false)
	private Integer newDrugId;

}
