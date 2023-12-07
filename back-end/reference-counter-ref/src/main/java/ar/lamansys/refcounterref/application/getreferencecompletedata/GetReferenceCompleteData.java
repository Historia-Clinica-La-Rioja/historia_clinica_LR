package ar.lamansys.refcounterref.application.getreferencecompletedata;

import ar.lamansys.refcounterref.application.getreferencecompletedata.exceptions.GetReferenceCompleteDataException;
import ar.lamansys.refcounterref.application.getreferencecompletedata.exceptions.GetReferenceCompleteDataExceptionEnum;
import ar.lamansys.refcounterref.application.port.HistoricReferenceRegulationStorage;
import ar.lamansys.refcounterref.application.port.ReferenceAppointmentStorage;
import ar.lamansys.refcounterref.application.port.ReferenceObservationStorage;
import ar.lamansys.refcounterref.application.port.ReferencePatientStorage;
import ar.lamansys.refcounterref.application.port.ReferenceStorage;
import ar.lamansys.refcounterref.domain.reference.ReferenceCompleteDataBo;
import ar.lamansys.refcounterref.domain.reference.ReferenceDataBo;
import ar.lamansys.refcounterref.domain.reference.ReferenceObservationBo;
import ar.lamansys.refcounterref.domain.reference.ReferencePatientBo;
import ar.lamansys.refcounterref.domain.referenceappointment.ReferenceAppointmentBo;
import ar.lamansys.refcounterref.domain.referenceregulation.ReferenceRegulationBo;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetReferenceCompleteData {

	private final ReferenceStorage referenceStorage;

	private final ReferenceAppointmentStorage referenceAppointmentStorage;

	private final ReferencePatientStorage referencePatientStorage;

	private final HistoricReferenceRegulationStorage historicReferenceRegulationStorage;
	
	private final ReferenceObservationStorage referenceObservationStorage;

	public ReferenceCompleteDataBo run(Integer referenceId) {
		log.debug("Input parameter -> referenceId {}", referenceId);
		assertInfo(referenceId);
		ReferenceDataBo referenceData = referenceStorage.getReferenceData(referenceId).orElseThrow(() ->
				new GetReferenceCompleteDataException(GetReferenceCompleteDataExceptionEnum.INVALID_REFERENCE_ID, "El identificador de la referencia es invalido"));

		ReferencePatientBo patientData = referencePatientStorage.getPatientInfo(referenceData.getPatientId());
		Optional<ReferenceAppointmentBo> appointmentData = referenceAppointmentStorage.getAppointmentData(referenceData.getId());
		setContactInformation(patientData, referenceData, appointmentData);
		Optional<ReferenceRegulationBo> referenceRegulation = historicReferenceRegulationStorage.getByReferenceId(referenceId);
		var observation = referenceObservationStorage.getReferenceObservation(referenceId).orElse(null);

		var result = new ReferenceCompleteDataBo(referenceData, patientData, appointmentData.orElse(null), referenceRegulation.orElse(null), observation);
		log.debug("Output -> {}", result);
		return result;
	}

	private void assertInfo(Integer referenceId) {
		Objects.requireNonNull(referenceId, () -> {
			throw new GetReferenceCompleteDataException(GetReferenceCompleteDataExceptionEnum.NULL_REFERENCE_ID, "El identificador de la referencia es obligatorio");
		});
	}

	private void setContactInformation(ReferencePatientBo patient, ReferenceDataBo reference, Optional<ReferenceAppointmentBo> appointment) {
		if (appointment.isPresent()) {
			var a = appointment.get();
			if (a.getPhonePrefix() != null && a.getPhoneNumber() != null) {
				patient.setPhonePrefix(a.getPhonePrefix());
				patient.setPhoneNumber(a.getPhoneNumber());
			} else {
				patient.setPhonePrefix(reference.getPhonePrefix());
				patient.setPhoneNumber(reference.getPhoneNumber());
			}
			if (a.getEmail() != null) {
				patient.setEmail(a.getEmail());
			}
		} else {
			patient.setPhonePrefix(reference.getPhonePrefix());
			patient.setPhoneNumber(reference.getPhoneNumber());
		}
	}

}
