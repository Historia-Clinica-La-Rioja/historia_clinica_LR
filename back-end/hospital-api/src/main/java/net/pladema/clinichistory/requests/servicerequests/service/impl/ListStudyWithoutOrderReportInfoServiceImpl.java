package net.pladema.clinichistory.requests.servicerequests.service.impl;

import ar.lamansys.sgh.clinichistory.domain.ips.StudyWithoutOrderReportInfoBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.repository.ListStudyWithoutOrderReportRepository;
import net.pladema.clinichistory.requests.servicerequests.service.ListStudyWithoutOrderReportInfoService;
import net.pladema.imagenetwork.application.getlocalviewerurl.GetLocalViewerUrl;
import net.pladema.imagenetwork.application.getpacwherestudyishosted.GetPacsWhereStudyIsHosted;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentDateHourBo;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ListStudyWithoutOrderReportInfoServiceImpl implements ListStudyWithoutOrderReportInfoService {

    private final ListStudyWithoutOrderReportRepository listStudyWithoutOrderReportRepository;
    private final GetPacsWhereStudyIsHosted getPacsWhereStudyIsHosted;
    private final AppointmentService appointmentService;
    private final GetLocalViewerUrl getLocalViewerUrl;

    @Override
    public List<StudyWithoutOrderReportInfoBo> execute(Integer patientId) {
        log.debug("Input -> patientId {}", patientId);
        List<StudyWithoutOrderReportInfoBo> result = listStudyWithoutOrderReportRepository.execute(patientId).stream()
                .map(this::createDiagnosticReportBo)
                .peek(studyWithoutOrderReportInfoBo -> studyWithoutOrderReportInfoBo.setIsAvailableInPACS(
                        getPacsWhereStudyIsHosted.run(studyWithoutOrderReportInfoBo.getImageId(), false)
                                .isAvailableInPACS()))
                .collect(Collectors.toList());
        addAppointmentDateTime(result);
        addLocalViewerUrl(result);
        log.debug("Output -> {}", result);
        return result;
    }

    private StudyWithoutOrderReportInfoBo createDiagnosticReportBo(Object[] row) {
        log.trace("Input parameters -> row {}", row);
        StudyWithoutOrderReportInfoBo result = new StudyWithoutOrderReportInfoBo();
        result.setStatus((Boolean) row[0]);
        result.setImageId((String) row[1]);
        result.setDocumentId((BigInteger) row[2]);
        result.setFileName((String) row[3]);
        result.setDocumentStatus((String) row[4]);
        result.setAppointmentId((Integer) row[5]);
		result.setReportStatusId((Short) row[6]);
		result.setDeriveToInstitution((String) row[7]);
        log.trace("Output -> {}", result);
        return result;
    }

    private void addAppointmentDateTime(Collection<StudyWithoutOrderReportInfoBo> result) {
        var uniqueIds = result.stream()
                .map(StudyWithoutOrderReportInfoBo::getAppointmentId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        Map<Integer, AppointmentDateHourBo> mappedAppointment =
                appointmentService.getAppointmentDateAndHourByIds(uniqueIds)
                        .stream()
                        .collect(Collectors.toMap(AppointmentDateHourBo::getAppointmentId, Function.identity()));
        result.forEach(reportInfoBo -> {
            if (Objects.nonNull(reportInfoBo.getAppointmentId()) && Objects.nonNull(mappedAppointment.get(reportInfoBo.getAppointmentId()))) {
                reportInfoBo.setAppointmentDate(mappedAppointment.get(reportInfoBo.getAppointmentId()).getAppointmentDate());
                reportInfoBo.setAppointmentHour(mappedAppointment.get(reportInfoBo.getAppointmentId()).getAppointmentTime());
            }
        });
    }

    private void addLocalViewerUrl(List<StudyWithoutOrderReportInfoBo> result) {
        var uniqueIds = result.stream()
                .map(StudyWithoutOrderReportInfoBo::getAppointmentId)
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