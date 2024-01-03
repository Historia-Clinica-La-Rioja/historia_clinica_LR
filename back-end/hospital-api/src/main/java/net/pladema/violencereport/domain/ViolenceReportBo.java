package net.pladema.violencereport.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ViolenceReportBo {

	private Integer patientId;

	private Short situationId;

	private Short evolutionId;

	private ViolenceReportVictimBo victimData;

	private ViolenceEpisodeDetailBo episodeData;

	private List<ViolenceReportAggressorBo> aggressors;

	private ViolenceReportImplementedActionsBo implementedActions;

	private String observation;

}
