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
public class CommercialMedicationArticleGtinPK implements Serializable {

	private static final long serialVersionUID = -7770699848591870227L;

	@Column(name = "article_id")
	private Integer articleId;

	@Column(name = "gtin")
	private String gtin;

}
