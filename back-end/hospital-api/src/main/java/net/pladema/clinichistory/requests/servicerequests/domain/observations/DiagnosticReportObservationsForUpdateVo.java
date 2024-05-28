package net.pladema.clinichistory.requests.servicerequests.domain.observations;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

import lombok.Getter;
import lombok.Value;
import net.pladema.clinichistory.requests.servicerequests.domain.observations.exceptions.ObservationToUpdateNotFound;
import net.pladema.clinichistory.requests.servicerequests.domain.observations.exceptions.InvalidProcedureTemplateChangeException;

@Getter
public class DiagnosticReportObservationsForUpdateVo {

	@Value
	private class DiagnosticReportObservationForUpdateVo {
		Integer id;
		Integer procedureParameterId;
		String value;
		Short unitOfMeasureId;
	}

	Integer diagnosticReportObservationGroupId;
	Integer procedureTemplateId;
	List<DiagnosticReportObservationForUpdateVo> existingObservations;

	public DiagnosticReportObservationsForUpdateVo(Integer procedureTemplateId, Integer diagnosticReportObservationGroupId) {
		this.procedureTemplateId = procedureTemplateId;
		this.diagnosticReportObservationGroupId = diagnosticReportObservationGroupId;
		this.existingObservations = new ArrayList<>();
	}

	public DiagnosticReportObservationsForUpdateVo addObservation(Integer id, Integer procedureParameterId, String value, Short unitOfMeasureId) {
		this.existingObservations.add(new DiagnosticReportObservationForUpdateVo(id, procedureParameterId, value, unitOfMeasureId));
		return this;
	}


	public List<UpdatedDiagnosticReportObservationBo> update(AddObservationsCommandVo updatedObservations) throws InvalidProcedureTemplateChangeException, ObservationToUpdateNotFound {
		List<UpdatedDiagnosticReportObservationBo> output = new ArrayList<>();

		if (!updatedObservations.getProcedureTemplateId().equals(this.getProcedureTemplateId())) {
			throw new InvalidProcedureTemplateChangeException();
		}

		var updates = updatedObservations.getValues();
		for (var update : updates) {
			var existing = findExisting(this.getExistingObservations(), update);
			if (update.isNumeric())
				output.add(new UpdatedDiagnosticReportObservationBo(existing.getId(), update.getValue(), update.getUnitOfMeasureId()));
			else
				output.add(new UpdatedDiagnosticReportObservationBo(existing.getId(), update.getValue()));
		}
		return output;
	}

	/**
	 * Match the updated observation with the one found in the database.
	 * Numeric observations special case:
	 * 	A numeric parameter may have more than one observation attached (See ProcedureParameter.inputCount).
	 * 	In this case the match with existing observations is done through parameter id and unit of measure id.
	 *
	 * @param existingObservations
	 * @param update
	 * @return
	 */
	private DiagnosticReportObservationForUpdateVo findExisting(
		List<DiagnosticReportObservationForUpdateVo> existingObservations,
		AddObservationsCommandVo.Observation update) throws ObservationToUpdateNotFound {
		Predicate<DiagnosticReportObservationForUpdateVo> filter = (x) -> {
			var sameParameterId = Objects.equals(x.getProcedureParameterId(), update.getProcedureParameterId());
			if (update.isNumeric()) {
				var sameUom = Objects.equals(x.getUnitOfMeasureId(), update.getUnitOfMeasureId());
				return sameUom && sameParameterId;
			}
			return sameParameterId;
		};

		return
		existingObservations.stream()
		.filter(filter)
		.findAny()
		.orElseThrow(() -> new ObservationToUpdateNotFound());
	}

}
