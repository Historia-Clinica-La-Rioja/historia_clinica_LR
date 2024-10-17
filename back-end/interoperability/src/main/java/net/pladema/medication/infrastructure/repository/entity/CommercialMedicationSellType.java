package net.pladema.medication.infrastructure.repository.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "sell_type", schema = "commercial_medication")
@Entity
public class CommercialMedicationSellType implements Serializable {

	private static final long serialVersionUID = -2091829916071848410L;
	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "description", length = 50)
	private String description;

}
