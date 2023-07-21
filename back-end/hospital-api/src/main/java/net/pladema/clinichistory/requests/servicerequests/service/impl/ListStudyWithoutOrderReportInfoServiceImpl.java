package net.pladema.clinichistory.requests.servicerequests.service.impl;

import ar.lamansys.sgh.clinichistory.domain.ips.StudyWithoutOrderReportInfoBo;
import net.pladema.clinichistory.requests.servicerequests.repository.ListStudyWithoutOrderReportRepository;
import net.pladema.clinichistory.requests.servicerequests.service.ListStudyWithoutOrderReportInfoService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ListStudyWithoutOrderReportInfoServiceImpl implements ListStudyWithoutOrderReportInfoService {

    ListStudyWithoutOrderReportRepository listStudyWithoutOrderReportRepository;

    private static final Logger LOG = LoggerFactory.getLogger(ListStudyWithoutOrderReportInfoServiceImpl.class);
    private static final String OUTPUT = "create result -> {}";

    public ListStudyWithoutOrderReportInfoServiceImpl(ListStudyWithoutOrderReportRepository listStudyWithoutOrderReportRepository) {
        this.listStudyWithoutOrderReportRepository = listStudyWithoutOrderReportRepository;
    }

    @Override
    public List<StudyWithoutOrderReportInfoBo> execute(Integer patientId) {
        List<StudyWithoutOrderReportInfoBo> result = listStudyWithoutOrderReportRepository.execute(patientId).stream()
                .map(this::createDiagnosticReportBo)
                .collect(Collectors.toList());
        LOG.trace("OUTPUT List -> {}", result);
        return result;
    }

    private StudyWithoutOrderReportInfoBo createDiagnosticReportBo(Object[] row) {
        LOG.debug("Input parameters -> row {}", row);
		StudyWithoutOrderReportInfoBo result = new StudyWithoutOrderReportInfoBo();
		result.setStatus((Boolean) row[0]);
        result.setImageId((String) row[1]);
        result.setDocumentId((BigInteger) row[2]);
		result.setFileName((String) row[3]);
		result.setDocumentStatus((String) row[4]);
        LOG.trace(OUTPUT, result);
        return result;
    }

}