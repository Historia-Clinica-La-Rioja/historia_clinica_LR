package net.pladema.violencereport.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ViolenceReportSituationEvolutionBo {

	private Short situationId;

	private Short evolutionId;

	private LocalDate episodeDate;

	private LocalDateTime createdOn;

	private Integer professionalPersonId;

	private String professionalFullName;

	public ViolenceReportSituationEvolutionBo(Short situationId, Short evolutionId, LocalDate episodeDate, LocalDateTime createdOn, Integer professionalPersonId) {
		this.situationId = situationId;
		this.evolutionId = evolutionId;
		this.episodeDate = episodeDate;
		this.createdOn = createdOn;
		this.professionalPersonId = professionalPersonId;
	}

}
