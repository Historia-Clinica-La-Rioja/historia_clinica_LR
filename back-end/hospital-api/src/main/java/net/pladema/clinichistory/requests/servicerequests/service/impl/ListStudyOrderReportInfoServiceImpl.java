package net.pladema.clinichistory.requests.servicerequests.service.impl;

import ar.lamansys.sgh.clinichistory.domain.ips.StudyOrderReportInfoBo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.clinichistory.requests.servicerequests.repository.ListStudyOrderReportRepository;
import net.pladema.clinichistory.requests.servicerequests.service.ListStudyOrderReportInfoService;
import net.pladema.imagenetwork.application.getpacwherestudyishosted.GetPacWhereStudyIsHosted;
import net.pladema.medicalconsultation.appointment.service.AppointmentService;
import net.pladema.medicalconsultation.appointment.service.domain.AppointmentDateHourBo;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ListStudyOrderReportInfoServiceImpl implements ListStudyOrderReportInfoService {

    private final ListStudyOrderReportRepository listStudyOrderReportRepository;
    private final GetPacWhereStudyIsHosted getPacWhereStudyIsHosted;
    private final AppointmentService appointmentService;

    @Override
    public List<StudyOrderReportInfoBo> getListStudyOrder(Integer patientId) {
        log.debug("Input -> patientId {}", patientId);
        List<StudyOrderReportInfoBo> result = listStudyOrderReportRepository.execute(patientId).stream()
                .map(this::createStudyOrderReportInfoBo)
                .peek(studyOrderReportInfoBo -> studyOrderReportInfoBo.setIsAvailableInPACS(
                        getPacWhereStudyIsHosted.run(studyOrderReportInfoBo.getImageId(), false)
                                .isAvailableInPACS()))
                .collect(Collectors.toList());
        addAppointmentDateTime(result);
        log.debug("Output -> {}", result);
        return result;
    }

    private StudyOrderReportInfoBo createStudyOrderReportInfoBo(Object[] row) {
        log.trace("Input parameters -> row {}", row);
        StudyOrderReportInfoBo result = new StudyOrderReportInfoBo();
        result.setStatus((Boolean) row[0]);
        result.setDoctorUserId((Integer) row[1]);
        result.setCreationDate(((Timestamp) row[2]).toLocalDateTime());
        result.setImageId((String) row[3]);
        result.setDocumentId((BigInteger) row[4]);
        result.setSnomed((String) row[5]);
        result.setHealthCondition((String) row[6]);
        result.setFileName((String) row[7]);
        result.setSource((String) row[8]);
        result.setServiceRequestId((Integer) row[9]);
        result.setDiagnosticReportId((Integer) row[10]);
        result.setAppointmentId((Integer) row[11]);
        result.setHasActiveAppointment((Boolean) row[12]);
        result.setObservationsFromServiceRequest((String) row[13]);
        log.trace("Output -> {}", result);
        return result;
    }

    private void addAppointmentDateTime(Collection<StudyOrderReportInfoBo> result) {
        var uniqueIds = result.stream()
                .map(StudyOrderReportInfoBo::getAppointmentId)
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
}
