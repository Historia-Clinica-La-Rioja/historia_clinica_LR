package net.pladema.medication.infrastructure.repository.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "quantity", schema = "commercial_medication")
@Entity
public class CommercialMedicationQuantity implements Serializable {

	private static final long serialVersionUID = 8592337900389690437L;

	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "description", length = 50)
	private String description;

}
