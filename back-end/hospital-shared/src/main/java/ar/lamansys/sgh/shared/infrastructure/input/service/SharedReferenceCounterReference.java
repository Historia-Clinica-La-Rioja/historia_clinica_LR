package ar.lamansys.sgh.shared.infrastructure.input.service;

import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.CompleteReferenceDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.CounterReferenceSummaryDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceCounterReferenceFileDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceProblemDto;

import java.time.LocalDate;
import java.util.List;

public interface SharedReferenceCounterReference {

    List<ReferenceCounterReferenceFileDto> getReferenceFilesData(Integer referenceId);

    CounterReferenceSummaryDto getCounterReference(Integer referenceId);

	List<Integer> saveReferences(List<CompleteReferenceDto> references);

    List<ReferenceProblemDto> getReferencesProblemsByPatient(Integer patientId);

	List<Integer> getProtectedAppointmentsIds(List<Integer> diaryIds);

	boolean isProtectedAppointment(Integer appointmentId);

	void updateProtectedAppointment(Integer appointment);

	boolean existsProtectedAppointmentInOpeningHour(Integer openingHourId);
}
