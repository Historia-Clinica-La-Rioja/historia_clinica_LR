package net.pladema.clinichistory.requests.transcribed.application.getbyappointmentid;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.transcribed.application.port.TranscribedServiceRequestStorage;
import net.pladema.clinichistory.requests.transcribed.domain.TranscribedServiceRequestBo;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetTranscribedServiceRequestByAppointmentId {

    private final TranscribedServiceRequestStorage transcribedServiceRequestStorage;

    public Optional<TranscribedServiceRequestBo> run(Integer appointmentId) {
        log.debug("Input -> appointmentId {}", appointmentId);
        Integer queryResult = transcribedServiceRequestStorage.getIdByAppointmentId(appointmentId);
        var result = queryResult != null ? this.buildBasicTranscribedServiceRequestBo(queryResult) : null;
        log.debug("Output -> {}", result);
        return Optional.ofNullable(result);
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
