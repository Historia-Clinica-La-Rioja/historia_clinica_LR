package net.pladema.reports.service;

import net.pladema.reports.controller.dto.FormVDto;
import net.pladema.reports.service.domain.FormVBo;

import java.time.ZonedDateTime;
import java.util.Map;

public interface FormReportService {

    FormVBo execute(Integer appointmentId);

    Map<String, Object> createContext(FormVDto reportDataDto);

    String createOutputFileName(Integer appointmentId, ZonedDateTime consultedDate);
}
