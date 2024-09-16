package net.pladema.clinichistory.requests.transcribed.infrastructure.output;

import ar.lamansys.sgh.clinichistory.domain.ips.DiagnosticReportBo;
import ar.lamansys.sgh.clinichistory.domain.ips.StudyTranscribedOrderReportInfoBo;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.transcribed.infrastructure.output.repository.ListTranscribedDiagnosticReportRepository;
import net.pladema.clinichistory.requests.transcribed.infrastructure.output.repository.TranscribedServiceRequestDiagnosticReportRepository;
import net.pladema.clinichistory.requests.transcribed.infrastructure.output.repository.TranscribedServiceRequestRepository;
import net.pladema.clinichistory.requests.transcribed.application.port.TranscribedServiceRequestStorage;
import net.pladema.clinichistory.requests.transcribed.domain.TranscribedServiceRequestBo;
import net.pladema.clinichistory.requests.transcribed.infrastructure.output.repository.entity.TranscribedServiceRequestDiagnosticReportPK;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TranscribedServiceRequestStorageImpl implements TranscribedServiceRequestStorage {

    private final TranscribedServiceRequestRepository transcribedServiceRequestRepository;
    private final ListTranscribedDiagnosticReportRepository listTranscribedDiagnosticReportRepository;
    private final TranscribedServiceRequestDiagnosticReportRepository transcribedServiceRequestDiagnosticReportRepository;

    @Override
    public TranscribedServiceRequestBo get(Integer transcribedServiceRequestId) {
        log.debug("Input parameter -> transcribedServiceRequestId {} ", transcribedServiceRequestId);
        return transcribedServiceRequestRepository.getTranscribedServiceRequest(transcribedServiceRequestId)
                .map(transcribedServiceRequestBo -> {
                    transcribedServiceRequestBo.setDiagnosticReports(this.getDiagnosticReports(transcribedServiceRequestId));
                    return transcribedServiceRequestBo;
                })
                .orElse(null);
    }

    @Override
    public Integer getIdByAppointmentId(Integer appointmentId) {
        log.debug("Input parameter -> appointmentId {} ", appointmentId);
        return listTranscribedDiagnosticReportRepository.getByAppointmentId(appointmentId);
    }

    @Override
    public List<StudyTranscribedOrderReportInfoBo> getListStudyTranscribedOrderReportInfo(Integer patientId) {
        log.debug("Input parameter -> patientId {} ", patientId);
        return listTranscribedDiagnosticReportRepository.getListTranscribedOrder(patientId)
                .stream()
                .map(this::createTranscribedDiagnosticReportInfoBo)
                .collect(Collectors.toList());
    }

    @Override
    public List<Integer> getIds(Integer patientId) {
        log.debug("Input parameter -> patientId {} ", patientId);
        return listTranscribedDiagnosticReportRepository.execute(patientId);
    }

    @Override
    public List<DiagnosticReportBo> getDiagnosticReports(Integer transcribedServiceRequestId) {
        log.debug("Input parameter -> transcribedServiceRequestId {} ", transcribedServiceRequestId);
        return transcribedServiceRequestRepository.getDiagnosticReports(transcribedServiceRequestId);
    }

    @Override
    public void deleteById(Integer transcribedServiceRequestId) {
        log.debug("Input parameter -> transcribedServiceRequestId {} ", transcribedServiceRequestId);
        transcribedServiceRequestRepository.deleteById(transcribedServiceRequestId);
    }

    @Override
    public void deleteDiagnosticReportIdRelatedTo(Integer transcribedServiceRequestId, Integer diagnosticReportId) {
        log.debug("Input parameter -> transcribedServiceRequestId {}, diagnosticReportId {}", transcribedServiceRequestId, diagnosticReportId);
        transcribedServiceRequestDiagnosticReportRepository.deleteById(new TranscribedServiceRequestDiagnosticReportPK(transcribedServiceRequestId, diagnosticReportId));
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
        result.setAppointmentId((Integer) row[8]);
		result.setReportStatusId((Short) row[9]);
		result.setDeriveToInstitution((String) row[10]);
        result.setDiagnosticReports(this.getDiagnosticReports(result.getServiceRequestId()));
        log.trace("Output -> {}", result);
        return result;
    }

}
