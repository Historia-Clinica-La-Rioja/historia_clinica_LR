package net.pladema.clinichistory.outpatient.application;

import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.application.markaserroraproblem.IsSameUserIdFromHealthCondition;
import ar.lamansys.sgh.clinichistory.application.markaserroraproblem.IsWithinExpirationTimeLimit;
import ar.lamansys.sgh.clinichistory.application.notes.NoteService;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;
import ar.lamansys.sgh.clinichistory.domain.ips.ProblemBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.DiagnosticReportRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.HealthConditionRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.ProblemErrorReasonRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.DiagnosticReport;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.HealthCondition;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.ProblemErrorReason;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionClinicalStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionVerificationStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.DiagnosticReportStatus;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.outpatient.application.exceptions.MarkAsErrorAProblemException;
import net.pladema.clinichistory.outpatient.application.exceptions.MarkAsErrorAProblemExceptionEnum;
import net.pladema.clinichistory.requests.servicerequests.repository.ServiceRequestRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.entity.ServiceRequest;
import net.pladema.clinichistory.requests.servicerequests.repository.entity.ServiceRequestCategory;
import net.pladema.clinichistory.requests.servicerequests.repository.entity.ServiceRequestStatus;
import net.pladema.clinichistory.requests.servicerequests.service.ListDiagnosticReportInfoService;
import net.pladema.clinichistory.requests.servicerequests.service.domain.DiagnosticReportFilterBo;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.medicalconsultation.appointment.service.AppointmentOrderImageService;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class MarkAsErrorAProblem {

    private final HealthConditionRepository healthConditionRepository;
    private final NoteService noteService;
    private final ProblemErrorReasonRepository problemErrorReasonRepository;
    private final IsSameUserIdFromHealthCondition isSameUserIdFromHealthCondition;
    private final IsWithinExpirationTimeLimit isWithinExpirationTimeLimit;
    private final ListDiagnosticReportInfoService listDiagnosticReportInfoService;
    private final DocumentService documentService;
    private final DiagnosticReportRepository diagnosticReportRepository;
    private final ServiceRequestRepository serviceRequestRepository;
    private final AppointmentService appointmentService;
    private final AppointmentOrderImageService appointmentOrderImageService;
    private final MessageSource messageSource;

    @Transactional
    public boolean run(Integer institutionId, Integer patientId, ProblemBo problem) {
        log.debug("Input parameters -> institutionId {}, patientId {}, problem {}", institutionId, patientId, problem);

        this.assertContextValid(problem.getId());

        var studiesRelatedToProblem = listDiagnosticReportInfoService.getList(
                        new DiagnosticReportFilterBo(patientId, null, null, null, null))
                .stream()
                .filter(diagnosticReportBo -> problem.getId().equals(diagnosticReportBo.getHealthCondition().getId()))
                .collect(Collectors.toList());

        studiesRelatedToProblem.stream()
                .filter(diagnosticReportBo -> diagnosticReportBo.getStatusId().equals(DiagnosticReportStatus.FINAL) ||
                        diagnosticReportBo.getStatusId().equals(DiagnosticReportStatus.FINAL_RDI))
                .findAny()
                .ifPresent((diagnoticReportCompleted) -> {
                    throw new MarkAsErrorAProblemException(MarkAsErrorAProblemExceptionEnum.HAS_AT_LEAST_ONE_STUDY_COMPLETED, "app.problems.error.has-at-least-one-study-completed");
                });

        var appointmentsRDI = getDiagnosticImagingOrders(studiesRelatedToProblem).stream()
                .map(appointmentOrderImageService::getAppointmentIdByOrderId)
                .flatMap(List::stream)
                .collect(Collectors.toList());

        appointmentsRDI.stream()
                .map(appointmentService::getAppointment)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .filter(appointmentBo -> appointmentBo.getAppointmentStateId().equals(AppointmentState.CONFIRMED) ||
                                         appointmentBo.getAppointmentStateId().equals(AppointmentState.SERVED))
                .findAny()
                .ifPresent((appointmentConfirmedOrServed) -> {
                    throw new MarkAsErrorAProblemException(MarkAsErrorAProblemExceptionEnum.HAS_AT_LEAST_ONE_APPOINTMENT_CONFIRMED_OR_SERVED, "app.problems.error.has-at-least-one-appointment-served-or-confirmed");
                });

        this.updateHealthCondition(problem);

        this.deleteServiceRequestDocuments(studiesRelatedToProblem);

        this.cancelAppointments(appointmentsRDI);

        log.debug("Output -> {}", true);
        return true;
    }

    private void updateHealthCondition(ProblemBo problem) {
        HealthCondition hc = this.healthConditionRepository.findById(problem.getId())
                .orElseThrow(() -> new NotFoundException("healthcondition-not-found", "Healthcondition not found"));

        hc.setStatusId(ConditionClinicalStatus.INACTIVE);
        hc.setVerificationStatusId(ConditionVerificationStatus.ERROR);
        hc.setInactivationDate(LocalDate.now());
        hc.setNoteId(noteService.createNote(problem.getErrorObservations()));
        this.problemErrorReasonRepository.save(new ProblemErrorReason(problem.getId(), problem.getErrorReasonId()));
        this.healthConditionRepository.save(hc);
    }

    private void assertContextValid(Integer problemId) {

        if (!isSameUserIdFromHealthCondition.run(problemId))
            throw new MarkAsErrorAProblemException(MarkAsErrorAProblemExceptionEnum.FORBIDDEN_USER_ID, "app.problems.error.not-same.user");

        if (!isWithinExpirationTimeLimit.run(problemId))
            throw new MarkAsErrorAProblemException(MarkAsErrorAProblemExceptionEnum.TIME_WINDOW_EXPIRATION, "app.problems.error.time-window.expiration");

    }

    private void deleteServiceRequestDocuments(List<DiagnosticReportBo> studiesRelatedToProblem) {
        studiesRelatedToProblem.stream()
                .map(DiagnosticReportBo::getId)
                .peek(this::updateDiagnosticReport)
                .map(documentService::getDocumentFromDiagnosticReport)
                .forEach(documentDiagnosticReport -> documentService.deleteById(documentDiagnosticReport.getDocumentId(), DocumentStatus.ERROR));

        studiesRelatedToProblem.stream()
                .map(DiagnosticReportBo::getEncounterId)
                .distinct()
                .peek(this::updateServiceRequest)
                .map(orderId -> documentService.getDocumentIdBySourceAndType(orderId, DocumentType.ORDER).get(0))
                .forEach(documentOrderId -> documentService.deleteById(documentOrderId, DocumentStatus.ERROR));
    }

    private void updateDiagnosticReport(Integer diagnosticReportId) {
        DiagnosticReport dr = diagnosticReportRepository.findById(diagnosticReportId)
                .orElseThrow(() -> new NotFoundException("diagnosticreport-not-found", "Diagnosticreport not found"));
        dr.setStatusId(DiagnosticReportStatus.ERROR);
        this.diagnosticReportRepository.save(dr);
    }

    private void updateServiceRequest(Integer orderId) {
        ServiceRequest sr = serviceRequestRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("servicerequest-not-found", "Servicerequest not found"));
        sr.setStatusId(ServiceRequestStatus.ERROR);
        this.serviceRequestRepository.save(sr);
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

    private void cancelAppointments(List<Integer> appIds) {
        appIds.forEach(appId -> appointmentService.updateState(appId, AppointmentState.CANCELLED,
                UserInfo.getCurrentAuditor(),
                messageSource.getMessage("app.problems.warning.cancel-appointments", null, LocaleContextHolder.getLocale())));
    }
}
