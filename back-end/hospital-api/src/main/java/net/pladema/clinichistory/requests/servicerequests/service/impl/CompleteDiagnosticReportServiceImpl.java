package net.pladema.clinichistory.requests.servicerequests.service.impl;

import ar.lamansys.sgh.clinichistory.domain.ips.services.LoadDiagnosticReports;
import ar.lamansys.sgh.clinichistory.domain.ips.services.SnomedService;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.DiagnosticReportRepository;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.entity.DiagnosticReport;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.DiagnosticReportStatus;
import ar.lamansys.sgh.clinichistory.application.document.DocumentService;
import ar.lamansys.sgh.shared.infrastructure.input.service.SharedReferencePort;
import ar.lamansys.sgh.shared.infrastructure.input.service.referencecounterreference.ReferenceClosureDto;
import net.pladema.clinichistory.requests.servicerequests.service.CompleteDiagnosticReportService;
import net.pladema.clinichistory.requests.servicerequests.service.domain.CompleteDiagnosticReportBo;
import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;

import net.pladema.clinichistory.requests.servicerequests.service.domain.ReferenceRequestClosureBo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.List;
import java.util.Optional;

@Service
public class CompleteDiagnosticReportServiceImpl implements CompleteDiagnosticReportService {

    private final DiagnosticReportRepository diagnosticReportRepository;
    private final DocumentService documentService;
    private final LoadDiagnosticReports loadDiagnosticReports;
    private final SnomedService snomedService;
	private final SharedReferencePort sharedReferencePort;

    private static final Logger LOG = LoggerFactory.getLogger(CompleteDiagnosticReportServiceImpl.class);
    private final String OUTPUT = "Output -> {}";

    public CompleteDiagnosticReportServiceImpl(DiagnosticReportRepository diagnosticReportRepository, DocumentService documentService,
                                               LoadDiagnosticReports loadDiagnosticReports, SnomedService snomedService,
                                               SharedReferencePort sharedReferencePort){
        this.diagnosticReportRepository = diagnosticReportRepository;
        this.documentService = documentService;
        this.loadDiagnosticReports = loadDiagnosticReports;
        this.snomedService = snomedService;
		this.sharedReferencePort = sharedReferencePort;
    }

    @Override
    public Integer run(Integer patientId, Integer diagnosticReportId, CompleteDiagnosticReportBo completeDiagnosticReportBo, Integer institutionId) {
        LOG.debug("input -> patientId {}, diagnosticReportId {}, completeDiagnosticReportBo {}", patientId, diagnosticReportId, completeDiagnosticReportBo);
        var dr = diagnosticReportRepository.findById(diagnosticReportId);
        Integer result = -1;
        if (dr.isPresent()){
			Assert.notNull(patientId, "El código identificador del paciente es obligatorio");
            assertCompleteDiagnosticReport(dr.get());

            DiagnosticReportBo diagnosticReportBo = getCompletedDiagnosticReport(dr.get(), completeDiagnosticReportBo);
            var documentDiagnosticReport = documentService.getDocumentFromDiagnosticReport(diagnosticReportId);
			result = loadDiagnosticReports.run(documentDiagnosticReport.getDocumentId(), patientId, Optional.of(diagnosticReportId), List.of(diagnosticReportBo)).get(0);
        }

		if (completeDiagnosticReportBo.getReferenceClosure() != null){
			sharedReferencePort.closeReference(mapToReferenceClosureDto(completeDiagnosticReportBo.getReferenceClosure()), institutionId, patientId);
		}
        LOG.debug(OUTPUT, result);
        return result;
    }

    private DiagnosticReportBo getCompletedDiagnosticReport(DiagnosticReport diagnosticReport, CompleteDiagnosticReportBo completeDiagnosticReportBo) {
        LOG.debug("Input parameters -> diagnosticReport {}, completeDiagnosticReportBo {} ", diagnosticReport, completeDiagnosticReportBo);
        DiagnosticReportBo result = new DiagnosticReportBo();

        result.setHealthConditionId(diagnosticReport.getHealthConditionId());
        result.setStatusId(DiagnosticReportStatus.FINAL);
        result.setObservations(completeDiagnosticReportBo.getObservations());
        result.setLink(completeDiagnosticReportBo.getLink());
        result.setSnomed(snomedService.getSnomed(diagnosticReport.getSnomedId()));
        return result;
    }

    private void assertCompleteDiagnosticReport(DiagnosticReport dr){
        Assert.isTrue(!dr.getStatusId().equals(DiagnosticReportStatus.FINAL), "El estudio con id "+ dr.getId() + " no se puede completar porque ya ha sido completado");
        Assert.isTrue(!dr.getStatusId().equals(DiagnosticReportStatus.CANCELLED), "El estudio con id "+ dr.getId() + " no se puede completar porque ha sido cancelado");
    }

	private ReferenceClosureDto mapToReferenceClosureDto (ReferenceRequestClosureBo bo){
		ReferenceClosureDto result = new ReferenceClosureDto();
		result.setReferenceId(bo.getReferenceId());
		result.setClosureTypeId(bo.getClosureTypeId());
		result.setCounterReferenceNote(bo.getCounterReferenceNote());
		result.setClinicalSpecialtyId(bo.getClinicalSpecialtyId());
		return result;
	}
}
