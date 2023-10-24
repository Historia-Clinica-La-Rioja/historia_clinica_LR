package net.pladema.clinichistory.requests.servicerequests.service.impl;

import ar.lamansys.sgh.clinichistory.domain.ips.TranscribedDiagnosticReportBo;
import ar.lamansys.sgh.clinichistory.domain.ips.TranscribedOrderReportInfoBo;
import net.pladema.clinichistory.requests.servicerequests.repository.ListTranscribedDiagnosticReportRepository;
import net.pladema.clinichistory.requests.servicerequests.service.ListTranscribedDiagnosticReportInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

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

	@Override
	public TranscribedDiagnosticReportBo getByAppointmentId(Integer patientId) {
		List<Object[]> queryResult = listTranscribedDiagnosticReportRepository.getByAppointmentId(patientId);
		List<TranscribedDiagnosticReportBo> result;
		if (queryResult.size() != 0) {
			result = queryResult.stream().map(this::createDiagnosticReportBo).collect(Collectors.toList());
			LOG.trace("OUTPUT List -> {}", result);
			return result.get(0);
		}
		return null;
	}

	public List<TranscribedOrderReportInfoBo> getListTranscribedOrder(Integer patientId) {
		List<TranscribedOrderReportInfoBo> result = listTranscribedDiagnosticReportRepository.getListTranscribedOrder(patientId).stream()
				.map(this::createTranscribedDiagnosticReportInfoBo)
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
        result.setStudyName((String) row[2]);
        LOG.trace(OUTPUT, result);
        return result;
    }
	private TranscribedOrderReportInfoBo createTranscribedDiagnosticReportInfoBo(Object [] row){
		LOG.debug("Input parameters -> row {}", row);
		TranscribedOrderReportInfoBo result = new TranscribedOrderReportInfoBo();
		result.setStatus((Boolean) row[0]);
		result.setProfessionalName((String)row[1]);
		result.setCreationDate(((Timestamp)row[2]).toLocalDateTime());
		result.setImageId((String)row[3]);
		result.setDocumentId((BigInteger)row[4]);
		result.setSnomed((String)row[5]);
		result.setHealthCondition((String)row[6]);
		result.setFileName((String)row[7]);
		result.setDocumentStatus((String)row[8]);
		return result;
	}
}