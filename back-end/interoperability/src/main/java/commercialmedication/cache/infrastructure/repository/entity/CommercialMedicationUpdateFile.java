package commercialmedication.cache.infrastructure.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "update_file", schema = "commercial_medication")
@Entity
public class CommercialMedicationUpdateFile implements Serializable {

	private static final long serialVersionUID = 5626820266663252786L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "log_id")
	private Long logId;

	@Column(name = "processed")
	private Boolean processed;

	public CommercialMedicationUpdateFile(Long logId) {
		this.logId = logId;
		this.processed = false;
	}
}
