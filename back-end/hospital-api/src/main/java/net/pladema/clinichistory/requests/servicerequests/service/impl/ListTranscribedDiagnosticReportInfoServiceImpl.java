package net.pladema.clinichistory.requests.servicerequests.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import ar.lamansys.sgh.clinichistory.domain.ips.TranscribedDiagnosticReportBo;
import net.pladema.clinichistory.requests.servicerequests.repository.ListTranscribedDiagnosticReportRepository;
import net.pladema.clinichistory.requests.servicerequests.service.ListTranscribedDiagnosticReportInfoService;

@Service
public class ListTranscribedDiagnosticReportInfoServiceImpl implements ListTranscribedDiagnosticReportInfoService {

    ListTranscribedDiagnosticReportRepository listTranscribedDiagnosticReportRepository;

    private static final Logger LOG = LoggerFactory.getLogger(ListTranscribedDiagnosticReportInfoServiceImpl.class);
    private static final String OUTPUT = "create result -> {}";

    public ListTranscribedDiagnosticReportInfoServiceImpl(ListTranscribedDiagnosticReportRepository listTranscribedDiagnosticReportRepository) {
        this.listTranscribedDiagnosticReportRepository = listTranscribedDiagnosticReportRepository;
    }

    @Override
    public List<TranscribedDiagnosticReportBo> execute(Integer patientId) {
        List<TranscribedDiagnosticReportBo> result = listTranscribedDiagnosticReportRepository.execute(patientId).stream()
                .map(this::createDiagnosticReportBo)
                .collect(Collectors.toList());
        LOG.trace("OUTPUT List -> {}", result);
        return result;
    }

    private TranscribedDiagnosticReportBo createDiagnosticReportBo(Object[] row) {
        LOG.debug("Input parameters -> row {}", row);
        TranscribedDiagnosticReportBo result = new TranscribedDiagnosticReportBo();
		result.setServiceRequestId((Integer) row[0]);
        result.setStudyId((Integer) row[1]);
        result.setStudyName((String) row[2]);
        LOG.trace(OUTPUT, result);
        return result;
    }
}