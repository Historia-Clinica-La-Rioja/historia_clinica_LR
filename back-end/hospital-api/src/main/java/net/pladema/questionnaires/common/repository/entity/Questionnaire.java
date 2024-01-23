package net.pladema.questionnaires.common.repository.entity;


import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "minsal_lr_questionnaire", schema = "public")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class Questionnaire {

	@JsonIgnore
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "name", length = 100, nullable = false)
	private String name;

	@JsonIgnore
	@Column(name = "status_id", nullable = false)
	private Integer statusId;

	@JsonIgnore
	@Column(name = "version")
	private Integer version;

	@JsonIgnore
	@Column(name = "description", length = 500)
	private String description;

	@JsonIgnore
	@Column(name = "created_on", nullable = false)
	private LocalDateTime createdOn;

	@JsonIgnore
	@Column(name = "updated_on", nullable = false)
	private LocalDateTime updatedOn;

	@JsonIgnore
	@Column(name = "deleted", nullable = false)
	private Boolean deleted;

	@Column(name = "deleted_on")
	private LocalDateTime deletedOn;
}
