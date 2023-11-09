package net.pladema.clinichistory.outpatient.application.markaserroraproblem;

import ar.lamansys.sgh.clinichistory.application.markaserroraproblem.IsSameUserIdFromHealthCondition;
import ar.lamansys.sgh.clinichistory.application.markaserroraproblem.IsWithinExpirationTimeLimit;
import ar.lamansys.sgh.clinichistory.domain.ips.ClinicalTerm;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;
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
import net.pladema.clinichistory.requests.servicerequests.repository.ServiceRequestRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.entity.ServiceRequest;
import net.pladema.clinichistory.requests.servicerequests.repository.entity.ServiceRequestCategory;
import net.pladema.clinichistory.requests.servicerequests.service.ListDiagnosticReportInfoService;
import net.pladema.clinichistory.requests.servicerequests.service.domain.DiagnosticReportFilterBo;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.medicalconsultation.appointment.service.AppointmentOrderImageService;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CanBeMarkAsError {

    private final IsSameUserIdFromHealthCondition isSameUserIdFromHealthCondition;
    private final IsWithinExpirationTimeLimit isWithinExpirationTimeLimit;
    private final ListDiagnosticReportInfoService listDiagnosticReportInfoService;
    private final ServiceRequestRepository serviceRequestRepository;
    private final AppointmentService appointmentService;
    private final AppointmentOrderImageService appointmentOrderImageService;
    private final HealthConditionRepository healthConditionRepository;

    public ProblemErrorBo run(Integer institutionId, Integer patientId, Integer healthConditionId) {
        log.debug("Input parameters -> institutionId {}, patientId {}, healthConditionId {}", institutionId, patientId, healthConditionId);

        this.assertContextValid(healthConditionId);

        var studies = listDiagnosticReportInfoService.getList(
                        new DiagnosticReportFilterBo(patientId, null, null, null, null))
                .stream()
                .filter(diagnosticReportBo -> healthConditionId.equals(diagnosticReportBo.getHealthCondition().getId()))
                .collect(Collectors.toList());

        this.assertContextValidDiagnosticReports(studies);

        var appointmentsRDI = this.getDiagnosticImagingOrders(studies).stream()
                .map(appointmentOrderImageService::getAppointmentIdByOrderId)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        this.assertContextValidAppointmentsRDI(appointmentsRDI);

        var serviceRequests = studies.stream()
                .map(DiagnosticReportBo::getEncounterId)
                .distinct()
                .collect(Collectors.toList());

        log.debug("Output -> {}", true);
        return new ProblemErrorBo(null, null,
                studies.stream().map(ClinicalTerm::getId).collect(Collectors.toList()),
                serviceRequests,
                appointmentsRDI);
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

    private void assertContextValidDiagnosticReports(List<DiagnosticReportBo> studiesRelatedToProblem) {
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

    private List<Integer> getDiagnosticImagingOrders(List<DiagnosticReportBo> studiesRelatedToProblem) {
        return studiesRelatedToProblem.stream()
                .map(DiagnosticReportBo::getEncounterId)
                .distinct()
                .map(serviceRequestRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(serviceRequest -> serviceRequest.getCategoryId().equals(ServiceRequestCategory.DIAGNOSTIC_IMAGING))
                .map(ServiceRequest::getId)
                .collect(Collectors.toList());
    }
}
