package ar.lamansys.sgh.clinichistory.domain.ips;

import ar.lamansys.sgh.clinichistory.domain.ips.enums.EPregnancyTermination;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.ObstetricEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ObstetricEventBo {

	private Integer id;

	private Short previousPregnancies;

	private LocalDate currentPregnancyEndDate;

	private Short gestationalAge;

	private EPregnancyTermination pregnancyTerminationType;

	private List<NewbornBo> newborns;

	public ObstetricEventBo(ObstetricEvent entity){
		this.id = entity.getId();
		this.previousPregnancies = entity.getPreviousPregnancies();
		this.currentPregnancyEndDate = entity.getCurrentPregnancyEndDate();
		this.gestationalAge = entity.getGestationalAge();
		this.pregnancyTerminationType = entity.getPregnancyTerminationType() != null ? EPregnancyTermination.map(entity.getPregnancyTerminationType()) : null;
	}

	public boolean hasNotNullValues(){
		return previousPregnancies != null ||
				currentPregnancyEndDate != null	||
				gestationalAge != null ||
				pregnancyTerminationType != null ||
				(newborns != null && !newborns.isEmpty());
	}

}
