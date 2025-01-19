package ar.lamansys.sgh.shared.application.encounter;

import java.util.Optional;

public interface SharedEncounterToAppointmentService {
	/**
	 * Translates the given encounterId to the corresponding
	 * appointmentId.
	 * <p>
	 * This should be called when fetching the annex report from outside
	 * the app, where the client only has the encounter id
	 * that they obtained through the ActivitiesController.
	 * <p>
	 * This was initially created to obtain the annex report from the
	 * addons app. See HSI-6603.
	 */
	public Optional<Integer> run(Integer encounterId, Short sourceTypeId) ;

	boolean sourceTypeSupported(Short sourceTypeId);
}
