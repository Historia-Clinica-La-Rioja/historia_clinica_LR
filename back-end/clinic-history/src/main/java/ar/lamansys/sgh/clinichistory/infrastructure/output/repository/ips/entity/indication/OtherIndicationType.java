package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.indication;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "other_indication_type")
@Getter
@Setter
@NoArgsConstructor
public class OtherIndicationType {

	private static final long serialVersionUID = 2873716268832417941L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Short id;

	@Column(name = "description", nullable = false, length = 100)
	private String description;

}
