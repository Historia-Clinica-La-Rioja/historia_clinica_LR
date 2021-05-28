package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "quantity")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Quantity {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Long id;

	@Column(name = "value", nullable = false)
	private Double value;

	@Column(name = "unit", length = 20, nullable = false)
	private String unit;

}
