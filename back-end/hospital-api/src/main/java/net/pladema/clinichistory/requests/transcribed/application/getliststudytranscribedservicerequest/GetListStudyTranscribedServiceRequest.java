package net.pladema.clinichistory.requests.transcribed.application.getliststudytranscribedservicerequest;

import ar.lamansys.sgh.clinichistory.domain.ips.StudyTranscribedOrderReportInfoBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.transcribed.application.port.TranscribedServiceRequestStorage;
import net.pladema.imagenetwork.application.getlocalviewerurl.GetLocalViewerUrl;
import net.pladema.imagenetwork.application.getpacwherestudyishosted.GetPacWhereStudyIsHosted;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentDateHourBo;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class GetListStudyTranscribedServiceRequest {

    private final GetPacWhereStudyIsHosted getPacWhereStudyIsHosted;
    private final TranscribedServiceRequestStorage transcribedServiceRequestStorage;
    private final AppointmentService appointmentService;
    private final GetLocalViewerUrl getLocalViewerUrl;

    public List<StudyTranscribedOrderReportInfoBo> run(Integer patientId) {
        log.debug("Input -> patientId {}", patientId);
        List<StudyTranscribedOrderReportInfoBo> result = transcribedServiceRequestStorage.getListStudyTranscribedOrderReportInfo(patientId)
                .stream()
                .peek(this::setIfIsAvailable)
                .collect(Collectors.toList());
        addAppointmentDateTime(result);
        addLocalViewerUrl(result);
        log.debug("Output -> {}", result);
        return result;
    }

    private void setIfIsAvailable(StudyTranscribedOrderReportInfoBo studyTranscribedOrderReportInfoBo) {
        var result = getPacWhereStudyIsHosted.run(studyTranscribedOrderReportInfoBo.getImageId(), false)
                .isAvailableInPACS();
        studyTranscribedOrderReportInfoBo.setIsAvailableInPACS(result);
    }

    private void addAppointmentDateTime(Collection<StudyTranscribedOrderReportInfoBo> result) {
        var uniqueIds = result.stream()
                .map(StudyTranscribedOrderReportInfoBo::getAppointmentId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<Integer, AppointmentDateHourBo> mappedAppointment =
                appointmentService.getAppointmentDateAndHourByIds(uniqueIds)
                        .stream()
                        .collect(Collectors.toMap(AppointmentDateHourBo::getAppointmentId, Function.identity()));

        result.forEach(reportInfoBo -> {
            if (Objects.nonNull(reportInfoBo.getAppointmentId())
                    && Objects.nonNull(mappedAppointment.get(reportInfoBo.getAppointmentId()))) {
                reportInfoBo.setAppointmentDate(mappedAppointment.get(reportInfoBo.getAppointmentId()).getAppointmentDate());
                reportInfoBo.setAppointmentHour(mappedAppointment.get(reportInfoBo.getAppointmentId()).getAppointmentTime());
            }
        });
    }

    private void addLocalViewerUrl(List<StudyTranscribedOrderReportInfoBo> result) {
        var uniqueIds = result.stream()
                .map(StudyTranscribedOrderReportInfoBo::getAppointmentId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<Integer,String> mappedUrls = getLocalViewerUrl.run(uniqueIds);

        result.forEach(reportInfoBo -> {
            if (Objects.nonNull(reportInfoBo.getAppointmentId())
                    && Objects.nonNull(mappedUrls.get(reportInfoBo.getAppointmentId()))) {
                reportInfoBo.setLocalViewerUrl(mappedUrls.get(reportInfoBo.getAppointmentId()));
            }
        });
    }

}
