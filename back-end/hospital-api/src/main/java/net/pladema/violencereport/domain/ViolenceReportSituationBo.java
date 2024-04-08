package net.pladema.violencereport.domain;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ViolenceReportSituationBo {

	private Short situationId;

	private List<String> violenceTypes;

	private List<String> violenceModalities;

	private Short riskLevelId;

	private LocalDate initialDate;

	private LocalDate lastModificationDate;

}
