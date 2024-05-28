package net.pladema.clinichistory.requests.servicerequests.domain.observations;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.function.BiFunction;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class AddObservationsCommandVo {
	@Setter
	@Getter
	@ToString
	public static class Observation {
		private Integer procedureParameterId;
		private @Getter String value;
		private @Getter Short unitOfMeasureId;
		private @Getter String snomedSctid;
		private @Getter String snomedPt;

		public boolean isNumeric() {
			return unitOfMeasureId != null;
		}
		public boolean isSnomed() {return (this.snomedSctid != null && this.snomedPt != null);}

		public void translateSnomed(BiFunction<String, String, String> snomedToValue) {
			if (this.isSnomed()) {
				this.value = snomedToValue.apply(snomedSctid, snomedPt);
			}
		}
	}

	private Boolean isPartialUpload;
	private Integer procedureTemplateId;
	private List<Observation> values;
}
