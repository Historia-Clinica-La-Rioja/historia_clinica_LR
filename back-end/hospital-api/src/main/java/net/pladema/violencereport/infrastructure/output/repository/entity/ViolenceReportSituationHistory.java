package net.pladema.violencereport.infrastructure.output.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Table(name = "violence_report_situation_history")
@Entity
public class ViolenceReportSituationHistory implements Serializable {

	private static final long serialVersionUID = 6556499411081610261L;

	@Id
	@Column(name = "report_id")
	private Integer reportId;

	@Column(name = "old_situation_id", nullable = false)
	private Short oldSituationId;

}
