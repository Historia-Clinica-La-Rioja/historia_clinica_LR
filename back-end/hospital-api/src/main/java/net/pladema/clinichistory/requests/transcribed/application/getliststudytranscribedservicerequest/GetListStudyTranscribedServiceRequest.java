package net.pladema.clinichistory.requests.transcribed.application.getliststudytranscribedservicerequest;

import ar.lamansys.sgh.clinichistory.domain.ips.StudyTranscribedOrderReportInfoBo;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.transcribed.application.port.TranscribedServiceRequestStorage;
import net.pladema.imagenetwork.application.getpacwherestudyishosted.GetPacWhereStudyIsHosted;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetListStudyTranscribedServiceRequest {

    private final GetPacWhereStudyIsHosted getPacWhereStudyIsHosted;
    private final TranscribedServiceRequestStorage transcribedServiceRequestStorage;

    public List<StudyTranscribedOrderReportInfoBo> run(Integer patientId) {
        log.debug("Input -> patientId {}", patientId);
        List<StudyTranscribedOrderReportInfoBo> result = transcribedServiceRequestStorage.getListStudyTranscribedOrderReportInfo(patientId)
                .stream()
                .peek(this::setIfIsAvailable)
                .collect(Collectors.toList());
        log.debug("Output -> {}", result);
        return result;
    }

    private void setIfIsAvailable(StudyTranscribedOrderReportInfoBo studyTranscribedOrderReportInfoBo) {
        var result = getPacWhereStudyIsHosted.run(studyTranscribedOrderReportInfoBo.getImageId(), false)
                .isAvailableInPACS();
        studyTranscribedOrderReportInfoBo.setIsAvailableInPACS(result);
    }
}
