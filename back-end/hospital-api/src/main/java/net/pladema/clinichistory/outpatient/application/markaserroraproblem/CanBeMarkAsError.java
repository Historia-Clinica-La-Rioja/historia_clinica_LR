package net.pladema.clinichistory.outpatient.application.markaserroraproblem;

import ar.lamansys.refcounterref.application.getreferencecompletedata.GetReferenceCompleteData;
import ar.lamansys.refcounterref.application.port.ReferenceHealthConditionStorage;
import ar.lamansys.refcounterref.domain.reference.ReferenceCompleteDataBo;
import ar.lamansys.refcounterref.domain.reference.ReferenceDataBo;
import ar.lamansys.refcounterref.domain.referenceappointment.ReferenceAppointmentBo;
import ar.lamansys.sgh.clinichistory.application.markaserroraproblem.IsSameUserIdFromHealthCondition;
import ar.lamansys.sgh.clinichistory.application.markaserroraproblem.IsWithinExpirationTimeLimit;
import ar.lamansys.sgh.clinichistory.domain.ips.ClinicalTerm;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.HealthConditionRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.HealthCondition;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionClinicalStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionVerificationStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.DiagnosticReportStatus;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.outpatient.application.markaserroraproblem.exceptions.MarkAsErrorAProblemException;
import net.pladema.clinichistory.outpatient.application.markaserroraproblem.exceptions.MarkAsErrorAProblemExceptionEnum;
import net.pladema.clinichistory.outpatient.domain.ProblemErrorBo;
import net.pladema.clinichistory.requests.servicerequests.application.GetDiagnosticReportResultsList;
import net.pladema.clinichistory.requests.servicerequests.application.port.ServiceRequestStorage;
import net.pladema.clinichistory.requests.servicerequests.domain.DiagnosticReportResultsBo;
import net.pladema.clinichistory.requests.servicerequests.domain.ServiceRequestProcedureInfoBo;
import net.pladema.clinichistory.requests.servicerequests.repository.ServiceRequestRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.entity.ServiceRequest;
import net.pladema.clinichistory.requests.servicerequests.repository.entity.ServiceRequestCategory;
import net.pladema.clinichistory.requests.servicerequests.service.domain.DiagnosticReportFilterBo;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.medicalconsultation.appointment.service.AppointmentOrderImageService;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CanBeMarkAsError {

    private final IsSameUserIdFromHealthCondition isSameUserIdFromHealthCondition;
    private final IsWithinExpirationTimeLimit isWithinExpirationTimeLimit;
    private final GetDiagnosticReportResultsList getDiagnosticReportResultsList;
    private final ServiceRequestRepository serviceRequestRepository;
    private final AppointmentService appointmentService;
    private final AppointmentOrderImageService appointmentOrderImageService;
    private final HealthConditionRepository healthConditionRepository;
    private final ReferenceHealthConditionStorage referenceHealthConditionStorage;
    private final GetReferenceCompleteData getReferenceCompleteData;
    private final ServiceRequestStorage serviceRequestStorage;

    public ProblemErrorBo run(Integer institutionId, Integer patientId, Integer healthConditionId) {
        log.debug("Input parameters -> institutionId {}, patientId {}, healthConditionId {}", institutionId, patientId, healthConditionId);

        this.assertContextValid(healthConditionId);

        var studies = getDiagnosticReportResultsList.run(
                        new DiagnosticReportFilterBo(patientId, null, null, null, null, null, null, null))
                .stream()
                .filter(diagnosticReportBo -> healthConditionId.equals(diagnosticReportBo.getHealthCondition().getId()))
                .collect(Collectors.toList());

        this.assertContextValidDiagnosticReports(studies);

        var appointmentsIds = this.getDiagnosticImagingOrders(studies).stream()
                .map(appointmentOrderImageService::getAppointmentIdByOrderId)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        this.assertContextValidAppointmentsRDI(appointmentsIds);

        var referenceIds = referenceHealthConditionStorage.getReferenceIds(healthConditionId);

        var completeReferences = referenceIds.stream()
                .map(getReferenceCompleteData::run)
                .collect(Collectors.toList());

        this.assertContextValidReferences(completeReferences);

        var serviceRequestReferenceIds = completeReferences.stream()
                .map(ReferenceCompleteDataBo::getReference)
                .map(ReferenceDataBo::getServiceRequestId)
                .collect(Collectors.toList());

        var indirectReferenceProcedures = serviceRequestStorage.getProceduresByServiceRequestIds(serviceRequestReferenceIds);

        this.assertContextValidIndirectProcedureReferences(indirectReferenceProcedures);

        var appointmentReferences = completeReferences.stream()
                .map(completeReference -> Optional.ofNullable(completeReference.getAppointment()))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        this.assertContextValidAppointmentsReferences(appointmentReferences);

        var diagnosticReportIds = studies.stream()
                .map(ClinicalTerm::getId)
                .collect(Collectors.toList());
        diagnosticReportIds.addAll(indirectReferenceProcedures.stream()
                .map(ServiceRequestProcedureInfoBo::getDiagnosticReportId)
                .collect(Collectors.toList()));

        var serviceRequestsIds = studies.stream()
                .map(DiagnosticReportResultsBo::getEncounterId)
                .distinct()
                .collect(Collectors.toList());

        appointmentsIds.addAll(appointmentReferences.stream()
                .map(ReferenceAppointmentBo::getAppointmentId)
                .collect(Collectors.toList()));

        log.debug("Output -> {}", true);
        return new ProblemErrorBo(null, null,
                diagnosticReportIds,
                serviceRequestsIds,
                appointmentsIds,
                referenceIds);
    }

    private void assertContextValid(Integer problemId) {

        HealthCondition hc = this.healthConditionRepository.findById(problemId)
                .orElseThrow(() -> new NotFoundException("healthcondition-not-found", "Healthcondition not found"));

        if (hc.getVerificationStatusId().equals(ConditionVerificationStatus.ERROR) || hc.getStatusId().equals(ConditionClinicalStatus.INACTIVE))
            throw new MarkAsErrorAProblemException(MarkAsErrorAProblemExceptionEnum.NOT_ACTIVE_OR_CHRONIC, "app.problems.error.not-active-or-chronic");

        if (!isSameUserIdFromHealthCondition.run(problemId))
            throw new MarkAsErrorAProblemException(MarkAsErrorAProblemExceptionEnum.FORBIDDEN_USER_ID, "app.problems.error.not-same.user");

        if (!isWithinExpirationTimeLimit.run(problemId))
            throw new MarkAsErrorAProblemException(MarkAsErrorAProblemExceptionEnum.TIME_WINDOW_EXPIRATION, "app.problems.error.time-window.expiration");

    }

    private void assertContextValidDiagnosticReports(List<DiagnosticReportResultsBo> studiesRelatedToProblem) {
        studiesRelatedToProblem.stream()
                .filter(diagnosticReportBo -> diagnosticReportBo.getStatusId().equals(DiagnosticReportStatus.FINAL) ||
                        diagnosticReportBo.getStatusId().equals(DiagnosticReportStatus.FINAL_RDI))
                .findAny()
                .ifPresent((diagnoticReportCompleted) -> {
                    throw new MarkAsErrorAProblemException(MarkAsErrorAProblemExceptionEnum.HAS_AT_LEAST_ONE_STUDY_COMPLETED, "app.problems.error.has-at-least-one-study-completed");
                });
    }

    private void assertContextValidAppointmentsRDI(List<Integer> appIds) {
        appIds.stream()
                .map(appointmentService::getEquipmentAppointment)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(appointmentBo -> appointmentBo.getAppointmentStateId().equals(AppointmentState.CONFIRMED) ||
                        appointmentBo.getAppointmentStateId().equals(AppointmentState.SERVED))
                .findAny()
                .ifPresent((appointmentConfirmedOrServed) -> {
                    throw new MarkAsErrorAProblemException(MarkAsErrorAProblemExceptionEnum.HAS_AT_LEAST_ONE_APPOINTMENT_CONFIRMED_OR_SERVED, "app.problems.error.has-at-least-one-appointment-served-or-confirmed");
                });
    }

    private List<Integer> getDiagnosticImagingOrders(List<DiagnosticReportResultsBo> studiesRelatedToProblem) {
        return studiesRelatedToProblem.stream()
                .map(DiagnosticReportResultsBo::getEncounterId)
                .distinct()
                .map(serviceRequestRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(serviceRequest -> serviceRequest.getCategoryId().equals(ServiceRequestCategory.DIAGNOSTIC_IMAGING))
                .map(ServiceRequest::getId)
                .collect(Collectors.toList());
    }

    private void assertContextValidReferences(List<ReferenceCompleteDataBo> referencesProblem) {
        referencesProblem.stream()
                .filter(completeReference -> Objects.nonNull(completeReference.getReference().getClosureType()))
                .findAny()
                .ifPresent((referenceAdvancedState) -> {
                    throw new MarkAsErrorAProblemException(MarkAsErrorAProblemExceptionEnum.REFERENCE_ADVANCED_STATE, "app.problems.error.reference-advanced-state");
                });
    }

    private void assertContextValidAppointmentsReferences(List<ReferenceAppointmentBo> appointmentReferences) {
        appointmentReferences.stream()
                .filter(referenceAppointmentBo -> referenceAppointmentBo.getAppointmentStateId().equals(AppointmentState.CONFIRMED) ||
                        referenceAppointmentBo.getAppointmentStateId().equals(AppointmentState.SERVED))
                .findAny()
                .ifPresent((appointmentConfirmedOrServed) -> {
                    throw new MarkAsErrorAProblemException(MarkAsErrorAProblemExceptionEnum.REFERENCE_ADVANCED_STATE, "app.problems.error.reference-advanced-state");
                });
    }

    private void assertContextValidIndirectProcedureReferences(List<ServiceRequestProcedureInfoBo> procedures) {
        procedures.stream()
                .filter(procedureInfo -> procedureInfo.getStatusId().equals(DiagnosticReportStatus.FINAL) ||
                        procedureInfo.getStatusId().equals(DiagnosticReportStatus.FINAL_RDI))
                .findAny()
                .ifPresent((procedureCompleted) -> {
                    throw new MarkAsErrorAProblemException(MarkAsErrorAProblemExceptionEnum.REFERENCE_ADVANCED_STATE, "app.problems.error.reference-advanced-state");
                });
    }
}
