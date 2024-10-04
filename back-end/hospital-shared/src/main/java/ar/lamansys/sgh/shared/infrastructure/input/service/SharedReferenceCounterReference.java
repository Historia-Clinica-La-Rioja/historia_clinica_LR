package ar.lamansys.sgh.shared.infrastructure.input.service;

import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.CompleteReferenceDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.CounterReferenceSummaryDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceAppointmentStateDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceCounterReferenceFileDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferencePhoneDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceProblemDto;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceRequestDto;

import java.util.List;
import java.util.Optional;

public interface SharedReferenceCounterReference {

    List<ReferenceCounterReferenceFileDto> getReferenceFilesData(Integer referenceId);

    Optional<CounterReferenceSummaryDto> getCounterReference(Integer referenceId);

	List<Integer> saveReferences(List<CompleteReferenceDto> references);

    List<ReferenceProblemDto> getReferencesProblemsByPatient(Integer patientId, List<Short> loggedUserRoleIds);

	List<Integer> getProtectedAppointmentsIds(List<Integer> diaryIds);

	boolean isProtectedAppointment(Integer appointmentId);

	boolean existsProtectedAppointmentInOpeningHour(Integer openingHourId);

	Optional<ReferenceRequestDto> getReferenceByServiceRequestId (Integer serviceRequestId);
	
	Optional<ReferenceAppointmentStateDto> getReferenceByAppointmentId(Integer appointmentId);

	void approveReferencesByRuleId(Integer ruleId, List<Integer> institutionIds);

	void updateRuleOnReferences(Integer ruleId, Short ruleLevel, List<Integer> ruleIdsToReplace);

	List<String> getReferenceClinicalSpecialtiesName(Integer referenceId);

	void updateProtectedAppointment(Integer appointment);

	void associateReferenceToAppointment(Integer referenceId, Integer appointmentId, boolean isProtected, Integer institutionId);

	ReferencePhoneDto getReferencePhoneData(Integer referenceId);
}
