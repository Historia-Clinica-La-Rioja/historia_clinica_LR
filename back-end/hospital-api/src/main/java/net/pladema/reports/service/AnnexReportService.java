package net.pladema.reports.service;

import net.pladema.reports.controller.dto.AnnexIIDto;
import net.pladema.reports.service.domain.AnnexIIBo;

import java.time.ZonedDateTime;
import java.util.Map;

public interface AnnexReportService {

    AnnexIIBo execute(Integer appointmentId);

    Map<String, Object> createContext(AnnexIIDto reportDataDto);

    String createOutputFileName(Integer appointmentId, ZonedDateTime consultedDate);
}
