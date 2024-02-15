package net.pladema.violencereport.domain;

import java.time.LocalDate;
import java.util.List;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.pladema.cipres.domain.SnomedBo;

@Getter
@Setter
@NoArgsConstructor
public class ViolenceEpisodeDetailBo {

	private LocalDate episodeDate;

	private List<SnomedBo> violenceTypeSnomedList;

	private List<SnomedBo> violenceModalitySnomedList;

	private Short violenceTowardsUnderageTypeId;

	private Boolean schooled;

	private Short schoolLevelId;

	private Short riskLevelId;

}
