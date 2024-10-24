package net.pladema.establishment.sanitaryresponsibilityarea.institutionresponsibilityarea.infrastructure.output.repository.entity;

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
public class InstitutionResponsibilityAreaPK implements Serializable {

	private static final long serialVersionUID = -6001982818618263786L;

	@Column(name = "institution_id")
	private Integer institutionId;

	@Column(name = "order_id")
	private Short orderId;

}
