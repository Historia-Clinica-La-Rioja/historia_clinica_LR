package commercial_medication.update_schema.cache.infrastructure.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "article_gtin", schema = "commercial_medication")
@Entity
public class CommercialMedicationArticleGtin implements Serializable {

	private static final long serialVersionUID = -6713397792469895549L;

	@EmbeddedId
	private CommercialMedicationArticleGtinPK pk;

}
