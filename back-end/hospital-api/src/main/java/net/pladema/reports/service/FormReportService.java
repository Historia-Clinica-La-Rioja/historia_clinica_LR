package net.pladema.reports.service;

import net.pladema.reports.controller.dto.FormVDto;
import net.pladema.reports.domain.FormVParametersBo;
import net.pladema.reports.service.domain.FormVBo;

import java.time.ZonedDateTime;
import java.util.Map;

public interface FormReportService {

    FormVBo getAppointmentData(FormVParametersBo parametersBo);

    FormVBo getConsultationData(FormVParametersBo parametersBo);

    Map<String, Object> createAppointmentContext(FormVDto reportDataDto);

    Map<String, Object> createConsultationContext(FormVDto reportDataDto);

    String createConsultationFileName(Long documentId, ZonedDateTime consultedDate);
}
