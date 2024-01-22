package net.pladema.questionnaires.general.create.repository.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import net.pladema.questionnaires.common.domain.QuestionnaireResponseII;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "minsal_lr_answer", schema = "public")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AnswerII {

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "questionnaire_response_id")
	private QuestionnaireResponseII questionnaireResponse;

	@Column(name = "item_id", nullable = false)
	private Integer itemId;

	@Column(name = "value", length = 100)
	private String value;

	@Column(name = "option_id")
	private Integer answerId;

}
