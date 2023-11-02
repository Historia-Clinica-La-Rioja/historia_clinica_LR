package net.pladema.clinichistory.outpatient.application;

import ar.lamansys.sgh.clinichistory.application.markaserroraproblem.IsSameUserIdFromHealthCondition;
import ar.lamansys.sgh.clinichistory.application.markaserroraproblem.IsWithinExpirationTimeLimit;
import ar.lamansys.sgh.clinichistory.application.notes.NoteService;
import ar.lamansys.sgh.clinichistory.domain.ips.ProblemBo;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.HealthConditionRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.ProblemErrorReasonRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.HealthCondition;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.ProblemErrorReason;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionClinicalStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionVerificationStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.DiagnosticReportStatus;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.outpatient.application.exceptions.MarkAsErrorAProblemException;
import net.pladema.clinichistory.outpatient.application.exceptions.MarkAsErrorAProblemExceptionEnum;
import net.pladema.clinichistory.requests.servicerequests.service.ListDiagnosticReportInfoService;
import net.pladema.clinichistory.requests.servicerequests.service.domain.DiagnosticReportFilterBo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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


        // execution and result
        this.updateHealthCondition(problem);

        // propagate
        // ...

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
        problemErrorReasonRepository.save(new ProblemErrorReason(problem.getId(), problem.getErrorReasonId()));
    }

    private void assertContextValid(Integer problemId) {

        if (!isSameUserIdFromHealthCondition.run(problemId))
            throw new MarkAsErrorAProblemException(MarkAsErrorAProblemExceptionEnum.FORBIDDEN_USER_ID, "app.problems.error.not-same.user");

        if (!isWithinExpirationTimeLimit.run(problemId))
            throw new MarkAsErrorAProblemException(MarkAsErrorAProblemExceptionEnum.TIME_WINDOW_EXPIRATION, "app.problems.error.time-window.expiration");

    }
}
