package net.pladema.medicalconsultation.appointment.controller.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class AttentionTypeReportDto {

    private final Short medicalAttentionTypeId;

    private final LocalTime openingHourFrom;

    private final LocalTime openingHourTo;

    private final List<AttentionTypeReportItemDto> appointments;

}
