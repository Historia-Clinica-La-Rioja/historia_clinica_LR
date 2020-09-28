package net.pladema.medicalconsultation.appointment.service.domain;

import lombok.Getter;
import net.pladema.medicalconsultation.appointment.repository.domain.DailyAppointmentVo;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class AttentionTypeReportBo {

    private final Short medicalAttentionTypeId;

    private final LocalTime openingHourFrom;

    private final LocalTime openingHourTo;

    private final List<AttentionTypeReportItemBo> appointments;

    public AttentionTypeReportBo(Short medicalAttentionTypeId, LocalTime openingHourFrom, LocalTime openingHourTo) {
        this.openingHourFrom = openingHourFrom;
        this.openingHourTo = openingHourTo;
        this.medicalAttentionTypeId = medicalAttentionTypeId;
        appointments = new ArrayList<>();
    }

    public void addAppointment(DailyAppointmentVo appointmentDiaryVo) {
        appointments.add(new AttentionTypeReportItemBo(appointmentDiaryVo));
    }

    public boolean isCompatibleWith(DailyAppointmentVo appointment){
        return (openingHourFrom.equals(appointment.getOpeningHourFrom())) &&
                (openingHourTo.equals(appointment.getOpeningHourTo())) &&
                (medicalAttentionTypeId.equals(appointment.getMedicalAttentionTypeId()));
    }
}
