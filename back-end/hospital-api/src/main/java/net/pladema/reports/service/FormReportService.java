package net.pladema.reports.service;

import net.pladema.reports.controller.dto.FormVDto;
import net.pladema.reports.service.domain.FormVBo;

import java.time.ZonedDateTime;
import java.util.Map;

public interface FormReportService {

    FormVBo getAppointmentData(Integer appointmentId);

    FormVBo getOutpatientData(Integer outpatientId);

    Map<String, Object> createAppointmentContext(FormVDto reportDataDto);

    Map<String, Object> createOutpatientContext(FormVDto reportDataDto);

    String createOutputFileName(Integer appointmentId, ZonedDateTime consultedDate);
}
