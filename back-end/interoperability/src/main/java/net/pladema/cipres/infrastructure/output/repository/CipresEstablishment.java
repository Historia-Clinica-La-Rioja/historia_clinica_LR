package net.pladema.cipres.infrastructure.output.repository;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "cipres_establishment")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CipresEstablishment implements Serializable {

	private static final long serialVersionUID = 3263691350126307022L;

	@EmbeddedId
	public CipresEstablishmentPk pk;

}
