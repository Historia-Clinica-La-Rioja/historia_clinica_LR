package ar.lamansys.sgh.shared.infrastructure.input.service;

import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.CompleteReferenceDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.CounterReferenceSummaryDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceCounterReferenceFileDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceProblemDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferencePhoneDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceRequestDto;

import java.util.List;
import java.util.Optional;

public interface SharedReferenceCounterReference {

    List<ReferenceCounterReferenceFileDto> getReferenceFilesData(Integer referenceId);

    Optional<CounterReferenceSummaryDto> getCounterReference(Integer referenceId);

	List<Integer> saveReferences(List<CompleteReferenceDto> references);

    List<ReferenceProblemDto> getReferencesProblemsByPatient(Integer patientId);

	List<Integer> getProtectedAppointmentsIds(List<Integer> diaryIds);

	boolean isProtectedAppointment(Integer appointmentId);

	void updateProtectedAppointment(Integer appointment);

	boolean existsProtectedAppointmentInOpeningHour(Integer openingHourId);

	ReferencePhoneDto getReferencePhone(Integer appointmentId);

	Optional<ReferenceRequestDto> getReferenceByServiceRequestId (Integer serviceRequestId);

}
