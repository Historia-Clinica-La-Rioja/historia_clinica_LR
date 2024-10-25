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
public class CommercialMedicationArticleAtcPK implements Serializable {

	private static final long serialVersionUID = 3258886657128137336L;

	@Column(name = "article_id")
	private Integer articleId;

	@Column(name = "atc_id")
	private String atcId;

}
