package net.pladema.clinichistory.requests.servicerequests.service.impl;

import ar.lamansys.sgh.clinichistory.domain.ips.TranscribedDiagnosticReportBo;
import ar.lamansys.sgh.clinichistory.domain.ips.StudyTranscribedOrderReportInfoBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.repository.ListTranscribedDiagnosticReportRepository;
import net.pladema.clinichistory.requests.servicerequests.service.ListTranscribedDiagnosticReportInfoService;
import net.pladema.imagenetwork.application.getpacwherestudyishosted.GetPacWhereStudyIsHosted;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ListTranscribedDiagnosticReportInfoServiceImpl implements ListTranscribedDiagnosticReportInfoService {

    private final ListTranscribedDiagnosticReportRepository listTranscribedDiagnosticReportRepository;
    private final GetPacWhereStudyIsHosted getPacWhereStudyIsHosted;

    @Override
    public List<TranscribedDiagnosticReportBo> execute(Integer patientId) {
        log.debug("Input -> patientId {}", patientId);
        List<TranscribedDiagnosticReportBo> result = listTranscribedDiagnosticReportRepository.execute(patientId).stream()
                .map(this::createDiagnosticReportBo)
                .collect(Collectors.toList());
        log.debug("Output -> {}", result);
        return result;
    }

    @Override
    public TranscribedDiagnosticReportBo getByAppointmentId(Integer appointmentId) {
        log.debug("Input -> appointmentId {}", appointmentId);
        List<Object[]> queryResult = listTranscribedDiagnosticReportRepository.getByAppointmentId(appointmentId);
        List<TranscribedDiagnosticReportBo> result;
        if (queryResult.size() != 0) {
            result = queryResult.stream().map(this::createDiagnosticReportBo).collect(Collectors.toList());
            log.debug("Output -> {}", result);
            return result.get(0);
        }
        return null;
    }

    public List<StudyTranscribedOrderReportInfoBo> getListTranscribedOrder(Integer patientId) {
        log.debug("Input -> patientId {}", patientId);
        List<StudyTranscribedOrderReportInfoBo> result = listTranscribedDiagnosticReportRepository.getListTranscribedOrder(patientId).stream()
                .map(this::createTranscribedDiagnosticReportInfoBo)
                .peek(studyTranscribedOrderReportInfoBo -> studyTranscribedOrderReportInfoBo.setIsAvailableInPACS(
                        getPacWhereStudyIsHosted.run(studyTranscribedOrderReportInfoBo.getImageId(), false)
                                .isAvailableInPACS()))
                .collect(Collectors.toList());
        log.debug("Output -> {}", result);
        return result;
    }

    private TranscribedDiagnosticReportBo createDiagnosticReportBo(Object[] row) {
        log.trace("Input parameters -> row {}", row);
        TranscribedDiagnosticReportBo result = new TranscribedDiagnosticReportBo();
        result.setServiceRequestId((Integer) row[0]);
        result.setStudyId((Integer) row[1]);
        result.setStudyName((String) row[2]);
        log.trace("Output -> {}", result);
        return result;
    }

    private StudyTranscribedOrderReportInfoBo createTranscribedDiagnosticReportInfoBo(Object[] row) {
        log.trace("Input parameters -> row {}", row);
        StudyTranscribedOrderReportInfoBo result = new StudyTranscribedOrderReportInfoBo();
        result.setStatus((Boolean) row[0]);
        result.setProfessionalName((String) row[1]);
        result.setCreationDate(((Timestamp) row[2]).toLocalDateTime());
        result.setImageId((String) row[3]);
        result.setDocumentId((BigInteger) row[4]);
        result.setSnomed((String) row[5]);
        result.setHealthCondition((String) row[6]);
        result.setFileName((String) row[7]);
        result.setDocumentStatus((String) row[8]);
        log.trace("Output -> {}", result);
        return result;
    }
}