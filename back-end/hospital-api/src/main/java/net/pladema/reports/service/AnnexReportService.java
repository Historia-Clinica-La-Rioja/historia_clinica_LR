package net.pladema.reports.service;

import net.pladema.reports.controller.dto.AnnexIIDto;
import net.pladema.reports.service.domain.AnnexIIBo;

import java.time.ZonedDateTime;
import java.util.Map;

public interface AnnexReportService {

    AnnexIIBo getAppointmentData(Integer appointmentId);

    AnnexIIBo getOutpatientData(Integer outpatientId);

    Map<String, Object> createAppointmentContext(AnnexIIDto reportDataDto);

    Map<String, Object> createOutpatientContext(AnnexIIDto reportDataDto);

    String createOutputFileName(Integer appointmentId, ZonedDateTime consultedDate);
}
