package ar.lamansys.sgh.shared.infrastructure.input.service;

import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.CounterReferenceSummaryDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceCounterReferenceFileDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceProblemDto;

import java.time.LocalDate;
import java.util.List;

public interface SharedReferenceCounterReference {

    List<ReferenceCounterReferenceFileDto> getReferenceFilesData(Integer referenceId);

    CounterReferenceSummaryDto getCounterReference(Integer referenceId);

    void saveReferences(Integer encounterId, Integer sourceTypeId, List<ReferenceDto> refrenceDto);

    List<ReferenceProblemDto> getReferencesProblemsByPatient(Integer patientId);

	Integer getAssignedProtectedAppointmentsQuantity(Integer diaryId, LocalDate day, Short appointmentStateId);

	List<Integer> getProtectedAppointmentsIds(List<Integer> diaryIds);

	boolean isProtectedAppointment(Integer appointmentId);

	void updateProtectedAppointment(Integer appointment);
}
