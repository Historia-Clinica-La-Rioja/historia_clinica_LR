package net.pladema.clinichistory.requests.servicerequests.domain.observations;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.IntStream;

import ar.lamansys.sgh.shared.domain.servicerequest.SharedAddObservationsCommandVo;
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


	public List<UpdatedDiagnosticReportObservationBo> update(SharedAddObservationsCommandVo updatedObservations) throws InvalidProcedureTemplateChangeException, ObservationToUpdateNotFound {
		List<UpdatedDiagnosticReportObservationBo> output = new ArrayList<>();

		if (!updatedObservations.getProcedureTemplateId().equals(this.getProcedureTemplateId())) {
			throw new InvalidProcedureTemplateChangeException();
		}

		var updates = updatedObservations.getValues();
		var existingObservationsCopy = this.getExistingObservations();

		for (var update : updates) {

			int existingIndex = findExistingIndex(existingObservationsCopy, update);
			var existing = existingObservationsCopy.remove(existingIndex);

			if (update.isNumeric())
				output.add(new UpdatedDiagnosticReportObservationBo(existing.getId(), update.getValue(), update.getUnitOfMeasureId(), update.getValueNumeric()));
			else
				output.add(new UpdatedDiagnosticReportObservationBo(existing.getId(), update.getValue()));
		}
		return output;
	}

	/**
	 * Match the updated observation with the one found in the database via parameter id.
	 * Numeric observations considerations:
	 * 	Numeric parameters may have more than one observation associated. For a given numeric parameter
	 * 	the observations are matched by order.
	 * 	Ex: the db has this:
	 * 		Observation id=1: procedureParameterId=1, value="", uomId=123
	 * 		Observation id=2: procedureParameterId=1, value="456", uomId=321
	 * 	The update id:
	 * 		procedureParameterId=1, value="45645", uomId=321
	 * 	 	procedureParameterId=1, value="456", uomId=567
	 *
	 * 	 The first line of the update will be matched with Observation id=1, while the second one, with
	 * 	 id=2. The final result will represent the intended update. The requirement is that the update
	 * 	 always has as many elements as observations are in the database.
	 *
	 * @param existingObservations
	 * @param update
	 * @return
	 */
	private Integer findExistingIndex(
		List<DiagnosticReportObservationForUpdateVo> existingObservations,
		SharedAddObservationsCommandVo.Observation update) throws ObservationToUpdateNotFound {
		return IntStream
			.range(0, existingObservations.size())
			.filter(idx -> Objects.equals(
				existingObservations.get(idx).getProcedureParameterId(),
				update.getProcedureParameterId())
			)
			.findFirst().orElseThrow(() -> new ObservationToUpdateNotFound());
	}
}
