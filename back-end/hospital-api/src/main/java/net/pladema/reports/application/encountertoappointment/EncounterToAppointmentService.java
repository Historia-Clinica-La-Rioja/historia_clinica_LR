package net.pladema.reports.application.encountertoappointment;

import java.util.Optional;

import org.springframework.stereotype.Service;

import ar.lamansys.sgh.shared.application.encounter.SharedEncounterToAppointmentService;
import lombok.RequiredArgsConstructor;
import net.pladema.medicalconsultation.appointment.service.DocumentAppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.DocumentAppointmentBo;
import net.pladema.reports.application.ports.EncounterToAppointmentPort;

@Service
@RequiredArgsConstructor
public class EncounterToAppointmentService implements SharedEncounterToAppointmentService {
	private final EncounterToAppointmentPort encounterToAppointmentPort;

	private final DocumentAppointmentService documentAppointmentService;

	/**
	 * See https://lamansys.atlassian.net/browse/HSI-6747
	 */
	@Override
	public Optional<Integer> run(Integer encounterId, Short sourceTypeId){
		if (encounterToAppointmentPort.supportsSourceType(sourceTypeId)){
			return encounterToAppointmentPort
				.encounterToDocumentId(encounterId, sourceTypeId)
				.flatMap(documentId -> documentAppointmentService.getDocumentAppointmentForDocument(documentId))
				.map(DocumentAppointmentBo::getAppointmentId);
		}
		else {
			return Optional.empty();
		}
	}

	@Override
	public boolean sourceTypeSupported(Short sourceTypeId) {
		return encounterToAppointmentPort.supportsSourceType(sourceTypeId);
	}

}
