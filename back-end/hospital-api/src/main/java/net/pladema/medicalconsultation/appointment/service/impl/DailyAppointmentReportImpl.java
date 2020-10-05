package net.pladema.medicalconsultation.appointment.service.impl;

import net.pladema.medicalconsultation.appointment.repository.DailyAppointmentRepository;
import net.pladema.medicalconsultation.appointment.repository.domain.DailyAppointmentVo;
import net.pladema.medicalconsultation.appointment.service.DailyAppointmentReport;
import net.pladema.medicalconsultation.appointment.service.domain.AttentionTypeReportBo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class DailyAppointmentReportImpl implements DailyAppointmentReport {

    private final Logger LOG = LoggerFactory.getLogger(DailyAppointmentReport.class);

    private final DailyAppointmentRepository dailyAppointmentRepository;

    public DailyAppointmentReportImpl(DailyAppointmentRepository dailyAppointmentRepository){
        this.dailyAppointmentRepository = dailyAppointmentRepository;
    }

    @Override
    public List<AttentionTypeReportBo> execute(Integer institutionId, Integer diaryId, LocalDate date) {
        LOG.debug("Input parameters -> institutionId {}, diaryId {}, date {}", institutionId, diaryId, date);
        List<DailyAppointmentVo> resultQuery = dailyAppointmentRepository.getDailyAppointmentsByDiaryIdAndDate(institutionId, diaryId, date);
        List<AttentionTypeReportBo> classifiedResult = separateByMedicalAttentionTypeAndAttentionTypeHours(resultQuery);
        LOG.debug("Output -> {}", classifiedResult);
        return classifiedResult;
    }

    private List<AttentionTypeReportBo> separateByMedicalAttentionTypeAndAttentionTypeHours(List<DailyAppointmentVo> appointments){
        LOG.debug("Input parameters -> appointments {}", appointments);
        List<AttentionTypeReportBo> result = new ArrayList<>();
        for (DailyAppointmentVo appointment: appointments){
            Optional<AttentionTypeReportBo> optAttentionTypeReport = findCorrespondingAttentionTypeReport(result, appointment);
            AttentionTypeReportBo attentionTypeReport;
            if (optAttentionTypeReport.isEmpty()){
                attentionTypeReport = new AttentionTypeReportBo(appointment.getMedicalAttentionTypeId(), appointment.getOpeningHourFrom(), appointment.getOpeningHourTo());
                result.add(attentionTypeReport);
            } else {
                attentionTypeReport = optAttentionTypeReport.get();
            }
            attentionTypeReport.addAppointment(appointment);
        }
        LOG.debug("Output -> {}", result);
        return result;
    }

    private Optional<AttentionTypeReportBo> findCorrespondingAttentionTypeReport(List<AttentionTypeReportBo> attentionTypeReports, DailyAppointmentVo appointment){
        LOG.debug("Input parameters -> attentionTypeReports {}, appointment {}", attentionTypeReports, appointment);
        Optional<AttentionTypeReportBo> correspondingReport = attentionTypeReports.stream()
                    .filter(reportItem -> reportItem.isCompatibleWith(appointment))
                    .findAny();
        LOG.debug("Output -> {}", correspondingReport);
        return correspondingReport.isPresent() ? correspondingReport : Optional.empty();
    }

}
