package ar.lamansys.sgh.shared.domain.servicerequest;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

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
		private @Getter BigDecimal valueNumeric;

		public boolean isNumeric() {
			return unitOfMeasureId != null;
		}
		public boolean isSnomed() {return (this.snomedSctid != null && this.snomedPt != null);}

		public void translateSnomed(BiFunction<String, String, String> snomedToValue) {
			if (this.isSnomed()) {
				this.value = snomedToValue.apply(snomedSctid, snomedPt);
			}
		}

		public boolean hasValue() {
			return value != null && !value.isEmpty();
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

	public void addValue(Integer procedureParameterId, String value, Short unitOfMeasureId, String snomedSctid, String snomedPt, BigDecimal valueNumeric) {
		if (this.values == null) values = new ArrayList<>();
		values.add(new Observation(procedureParameterId, value, unitOfMeasureId, snomedSctid, snomedPt, valueNumeric));
	}
	public List<Integer> getParameterIds() {
		return this.getValues()
		.stream()
		.map(Observation::getProcedureParameterId)
		.collect(Collectors.toList());
	}
}
