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
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class CommercialMedicationArticleBarCodePK implements Serializable {

	private static final long serialVersionUID = 6335585770816827686L;

	@Column(name = "article_id", nullable = false)
	private Integer articleId;

	@Column(name = "bar_code", nullable = false)
	private String barCode;

}
