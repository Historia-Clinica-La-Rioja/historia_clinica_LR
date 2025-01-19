package net.pladema.clinichistory.outpatient.application.markaserroraproblem;

import ar.lamansys.refcounterref.application.deletereference.DeleteReference;
import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.clinichistory.application.notes.NoteService;
import ar.lamansys.sgh.clinichistory.application.rebuildFile.RebuildFile;
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
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.EProblemErrorReason;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import ar.lamansys.sgx.shared.security.UserInfo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.outpatient.application.markaserroraproblem.exceptions.MarkAsErrorAProblemException;
import net.pladema.clinichistory.outpatient.application.markaserroraproblem.exceptions.MarkAsErrorAProblemExceptionEnum;
import net.pladema.clinichistory.outpatient.application.port.output.GetOdontologyDocumentIdPort;
import net.pladema.clinichistory.outpatient.application.port.output.UpdateLastOdontogramDrawingFromHistoricPort;
import net.pladema.clinichistory.outpatient.domain.ProblemErrorBo;
import net.pladema.clinichistory.outpatient.repository.OutpatientConsultationRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.ServiceRequestRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.entity.ServiceRequest;
import net.pladema.clinichistory.requests.servicerequests.repository.entity.ServiceRequestStatus;
import net.pladema.medicalconsultation.appointment.repository.entity.AppointmentState;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class MarkAsErrorAProblem {

    private final HealthConditionRepository healthConditionRepository;
    private final NoteService noteService;
    private final ProblemErrorReasonRepository problemErrorReasonRepository;
    private final DocumentService documentService;
    private final DiagnosticReportRepository diagnosticReportRepository;
    private final ServiceRequestRepository serviceRequestRepository;
    private final AppointmentService appointmentService;
    private final MessageSource messageSource;
    private final DeleteReference deleteReference;
    private final OutpatientConsultationRepository outpatientConsultationRepository;
    private final RebuildFile rebuildFile;
	private final UpdateLastOdontogramDrawingFromHistoricPort updateLastOdontogramDrawingFromHistoricPort;
	private final GetOdontologyDocumentIdPort getOdontologyDocumentIdPort;

    @Transactional
    public boolean run(Integer institutionId, Integer patientId, ProblemErrorBo problem) {
        log.debug("Input parameters -> institutionId {}, patientId {}, problem {}", institutionId, patientId, problem);

        this.assertContextValid(problem);

        this.updateHealthCondition(problem);

        problem.getDiagnosticReportsId()
                .forEach(this::updateDiagnosticReport);

        this.deleteServiceRequestDocuments(problem.getServiceRequestsId());

        this.cancelAppointments(problem.getAppointmentsId());

        this.cancelReferences(problem.getReferencesId());

        this.regenerateOutpatientDocument(problem.getId());
		updateLastOdontogramDrawingFromHistoricPort.run(patientId, problem.getId());
		regenerateOdontologyDocument(problem.getId());
        log.debug("Output -> {}", true);
        return true;
    }

    private void assertContextValid(ProblemErrorBo problem) {
        EProblemErrorReason.map(problem.getErrorReasonId());
    }

    private void updateHealthCondition(ProblemErrorBo problem) {
        HealthCondition hc = this.healthConditionRepository.findById(problem.getId())
                .orElseThrow(() -> new NotFoundException("healthcondition-not-found", "Healthcondition not found"));

        if (hc.getVerificationStatusId().equals(ConditionVerificationStatus.ERROR) || hc.getStatusId().equals(ConditionClinicalStatus.INACTIVE))
            throw new MarkAsErrorAProblemException(MarkAsErrorAProblemExceptionEnum.NOT_ACTIVE_OR_CHRONIC, "app.problems.error.not-active-or-chronic");

        hc.setStatusId(ConditionClinicalStatus.INACTIVE);
        hc.setVerificationStatusId(ConditionVerificationStatus.ERROR);
        hc.setInactivationDate(LocalDate.now());
        hc.setNoteId(noteService.createNote(problem.getErrorObservations()));
        this.problemErrorReasonRepository.save(new ProblemErrorReason(problem.getId(), problem.getErrorReasonId()));
        this.healthConditionRepository.save(hc);
    }

    private void deleteServiceRequestDocuments(List<Integer> ordersRelatedToProblem) {
        ordersRelatedToProblem.stream()
                .peek(this::updateServiceRequest)
                .map(orderId -> documentService.getDocumentIdBySourceAndType(orderId, DocumentType.ORDER).get(0))
                .forEach(documentOrderId -> documentService.deleteById(documentOrderId, DocumentStatus.ERROR));
    }

    private void updateDiagnosticReport(Integer diagnosticReportId) {
        DiagnosticReport dr = diagnosticReportRepository.findById(diagnosticReportId)
                .orElseThrow(() -> new NotFoundException("diagnosticreport-not-found", "Diagnosticreport not found"));
        dr.setStatusId(DiagnosticReportStatus.ERROR);
        dr = this.diagnosticReportRepository.save(dr);
        log.debug("Save success -> diagnoticReport[id={}, statusId={}]", dr.getId(), dr.getStatusId());
    }

    private void updateServiceRequest(Integer orderId) {
        ServiceRequest sr = serviceRequestRepository.findById(orderId)
                .orElseThrow(() -> new NotFoundException("servicerequest-not-found", "Servicerequest not found"));
        sr.setStatusId(ServiceRequestStatus.ERROR);
        sr = this.serviceRequestRepository.save(sr);
        log.debug("Save success -> serviceRequest[id={}, statusId={}]", sr.getId(), sr.getStatusId());
    }

    private void cancelAppointments(List<Integer> appIds) {
        appIds.forEach(appId -> appointmentService.updateState(appId, AppointmentState.CANCELLED,
                UserInfo.getCurrentAuditor(),
                messageSource.getMessage("app.problems.warning.cancel-appointments", null, LocaleContextHolder.getLocale())));
    }

    private void cancelReferences(List<Integer> referenceIds) {
        referenceIds.forEach(deleteReference::run);
    }

    private void regenerateOutpatientDocument(Integer healthConditionId) {
        outpatientConsultationRepository.getOutpatientConsultationDocument(healthConditionId).ifPresent((rebuildFile::run));
    }

	private void regenerateOdontologyDocument(Integer healthConditionId) {
		getOdontologyDocumentIdPort.run(healthConditionId).ifPresent(rebuildFile::run);
	}
}
