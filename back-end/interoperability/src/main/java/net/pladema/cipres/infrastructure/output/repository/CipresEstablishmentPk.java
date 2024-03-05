package net.pladema.cipres.infrastructure.output.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class CipresEstablishmentPk implements Serializable {

	private static final long serialVersionUID = -8549141482462629901L;

	@Column(name = "sisa_code", nullable = false)
	private String sisaCode;

	@Column(name = "cipres_establishment_id")
	private Integer cipresEstablishmentId;

}
