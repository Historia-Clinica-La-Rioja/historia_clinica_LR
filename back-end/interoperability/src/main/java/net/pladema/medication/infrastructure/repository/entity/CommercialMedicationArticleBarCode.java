package net.pladema.medication.infrastructure.repository.entity;

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
@Table(name = "article_bar_code", schema = "commercial_medication")
@Entity
public class CommercialMedicationArticleBarCode implements Serializable {

	private static final long serialVersionUID = 5559225835669840000L;

	@EmbeddedId
	private CommercialMedicationArticleBarCodePK pk;

}
