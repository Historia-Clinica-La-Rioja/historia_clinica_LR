package ar.lamansys.sgh.shared.domain.servicerequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class SharedAddObservationsCommandVo {
	@Setter
	@Getter
	@ToString
	@AllArgsConstructor
	@NoArgsConstructor
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

	@Getter
	@Setter
	@NoArgsConstructor
	@AllArgsConstructor
	@ToString
	public static class SharedReferenceRequestClosureBo {

		private Integer referenceId;
		private Integer clinicalSpecialtyId;
		private String counterReferenceNote;
		private Short closureTypeId;
		private List<Integer> fileIds;

	}

	private Boolean isPartialUpload;
	private Integer procedureTemplateId;
	private List<Observation> values;
	private SharedReferenceRequestClosureBo referenceClosure;

	public void addValue(Integer procedureParameterId, String value, Short unitOfMeasureId, String snomedSctid, String snomedPt) {
		if (this.values == null) values = new ArrayList<>();
		values.add(new Observation(procedureParameterId, value, unitOfMeasureId, snomedSctid, snomedPt));
	}
}
