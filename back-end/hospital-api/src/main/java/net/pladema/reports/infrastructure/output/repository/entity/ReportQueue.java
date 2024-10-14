package net.pladema.reports.infrastructure.output.repository.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "report_queue")
public class ReportQueue {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "created_on", nullable = false)
	private LocalDateTime createdOn;

	@Column(name = "generated_on")
	private LocalDateTime generatedOn;

	@Column(name = "generated_error", length = 512)
	private String generatedError;

	@Column(name = "file_id")
	private Long fileId;

}
