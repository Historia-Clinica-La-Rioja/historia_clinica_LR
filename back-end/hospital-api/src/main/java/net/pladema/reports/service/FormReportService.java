package net.pladema.reports.service;

import net.pladema.reports.controller.dto.FormVDto;

import java.time.ZonedDateTime;
import java.util.Map;

public interface FormReportService {

    net.pladema.reports.service.domain.FormVBo getAppointmentData(Integer appointmentId);

    net.pladema.reports.service.domain.FormVBo getConsultationData(Long documentId);

    Map<String, Object> createAppointmentContext(FormVDto reportDataDto);

    Map<String, Object> createConsultationContext(FormVDto reportDataDto);

    String createConsultationFileName(Long documentId, ZonedDateTime consultedDate);
}
