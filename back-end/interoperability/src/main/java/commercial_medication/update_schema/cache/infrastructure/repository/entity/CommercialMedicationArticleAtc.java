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
@Table(name = "article_atc", schema = "commercial_medication")
@Entity
public class CommercialMedicationArticleAtc implements Serializable {

	private static final long serialVersionUID = 2517616855683175924L;

	@EmbeddedId
	private CommercialMedicationArticleAtcPK pk;

}
