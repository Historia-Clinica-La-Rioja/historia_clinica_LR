package net.pladema.questionnaires.common.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "minsal_lr_answer", schema = "public")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Answer {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@Column(name = "questionnaire_response_id", nullable = false, insertable = false, updatable = false)
	private Integer questionnaireResponseId;

	@Column(name = "item_id", nullable = false)
	private Integer itemId;

	@Column(name = "value", length = 100)
	private String value;

	@Column(name = "option_id")
	private Integer answerId;
}
