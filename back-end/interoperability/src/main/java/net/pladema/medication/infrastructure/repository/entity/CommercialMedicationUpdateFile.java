package net.pladema.medication.infrastructure.repository.entity;

import lombok.AllArgsConstructor;
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
@AllArgsConstructor
@Table(name = "update_file", schema = "commercial_medication")
@Entity
public class CommercialMedicationUpdateFile implements Serializable {

	private static final long serialVersionUID = 5626820266663252786L;

	@Id
	@Column(name = "log_id")
	private Long logId;

	@Column(name = "file_path")
	private String filePath;

	@Column(name = "processed")
	private Boolean processed;

	public CommercialMedicationUpdateFile(Long logId) {
		this.logId = logId;
		this.processed = false;
	}
}
