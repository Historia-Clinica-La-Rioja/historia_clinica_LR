package net.pladema.reports.service;

import net.pladema.reports.controller.dto.AnnexIIDto;
import net.pladema.reports.domain.AnnexIIParametersBo;
import net.pladema.reports.service.domain.AnnexIIBo;

import java.time.ZonedDateTime;
import java.util.Map;

public interface AnnexReportService {

    AnnexIIBo getAppointmentData(AnnexIIParametersBo parametersBo);

    AnnexIIBo getConsultationData(AnnexIIParametersBo parametersBo);

    Map<String, Object> createAppointmentContext(AnnexIIDto reportDataDto);

    Map<String, Object> createConsultationContext(AnnexIIDto reportDataDto);

    String createConsultationFileName(Long documentId, ZonedDateTime consultedDate);

}
