package net.pladema.clinichistory.requests.servicerequests.service.impl;

import ar.lamansys.sgh.clinichistory.domain.ips.StudyTranscribedOrderReportInfoBo;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.repository.ListTranscribedDiagnosticReportRepository;
import net.pladema.clinichistory.requests.servicerequests.repository.TranscribedServiceRequestRepository;
import net.pladema.clinichistory.requests.servicerequests.service.ListTranscribedDiagnosticReportInfoService;
import net.pladema.clinichistory.requests.servicerequests.service.domain.TranscribedServiceRequestBo;
import net.pladema.imagenetwork.application.getpacwherestudyishosted.GetPacWhereStudyIsHosted;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class ListTranscribedDiagnosticReportInfoServiceImpl implements ListTranscribedDiagnosticReportInfoService {

    private final ListTranscribedDiagnosticReportRepository listTranscribedDiagnosticReportRepository;
    private final GetPacWhereStudyIsHosted getPacWhereStudyIsHosted;
    private final TranscribedServiceRequestRepository transcribedServiceRequestRepository;

    @Override
    public List<TranscribedServiceRequestBo> execute(Integer patientId) {
        log.debug("Input -> patientId {}", patientId);
        List<TranscribedServiceRequestBo> result = listTranscribedDiagnosticReportRepository.execute(patientId).stream()
                .map(this::buildBasicTranscribedServiceRequestBo)
                .collect(Collectors.toList());
        log.debug("Output -> {}", result);
        return result;
    }

    @Override
    public Optional<TranscribedServiceRequestBo> getByAppointmentId(Integer appointmentId) {
        log.debug("Input -> appointmentId {}", appointmentId);
        Integer queryResult = listTranscribedDiagnosticReportRepository.getByAppointmentId(appointmentId);
        var result = queryResult != null ? this.buildBasicTranscribedServiceRequestBo(queryResult) : null;
        log.debug("Output -> {}", result);
        return Optional.ofNullable(result);
    }

    @Override
    public List<StudyTranscribedOrderReportInfoBo> getListStudyTranscribedOrderReports(Integer patientId) {
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

    private TranscribedServiceRequestBo buildBasicTranscribedServiceRequestBo(Integer transcribedServiceRequestId) {
        log.trace("Input parameters -> transcribedServiceRequestId {}", transcribedServiceRequestId);
        TranscribedServiceRequestBo result = new TranscribedServiceRequestBo();
        result.setId(transcribedServiceRequestId);
        result.setDiagnosticReports(transcribedServiceRequestRepository.getDiagnosticReports(transcribedServiceRequestId));
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
        result.setFileName((String) row[5]);
        result.setDocumentStatus((String) row[6]);
        result.setServiceRequestId((Integer) row[7]);
        result.setDiagnosticReports(transcribedServiceRequestRepository.getDiagnosticReports(result.getServiceRequestId()));
        log.trace("Output -> {}", result);
        return result;
    }
}