package net.pladema.clinichistory.requests.transcribed.application.getlistsummary;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.transcribed.application.port.TranscribedServiceRequestStorage;
import net.pladema.clinichistory.requests.transcribed.domain.TranscribedServiceRequestBo;
import org.springframework.stereotype.Service;


@Slf4j
@RequiredArgsConstructor
@Service
public class GetListTranscribedServiceRequestSummary {

    private final TranscribedServiceRequestStorage transcribedServiceRequestStorage;

    public List<TranscribedServiceRequestBo> run(Integer patientId) {
        log.debug("Input -> patientId {}", patientId);
        List<TranscribedServiceRequestBo> result = transcribedServiceRequestStorage.getIds(patientId)
                .stream()
                .map(this::buildBasicTranscribedServiceRequestBo)
                .collect(Collectors.toList());
        log.debug("Output -> {}", result);
        return result;
    }

    private TranscribedServiceRequestBo buildBasicTranscribedServiceRequestBo(Integer transcribedServiceRequestId) {
        log.trace("Input parameters -> transcribedServiceRequestId {}", transcribedServiceRequestId);
        TranscribedServiceRequestBo result = new TranscribedServiceRequestBo();
        result.setId(transcribedServiceRequestId);
        result.setDiagnosticReports(transcribedServiceRequestStorage.getDiagnosticReports(transcribedServiceRequestId));
        log.trace("Output -> {}", result);
        return result;
    }
}