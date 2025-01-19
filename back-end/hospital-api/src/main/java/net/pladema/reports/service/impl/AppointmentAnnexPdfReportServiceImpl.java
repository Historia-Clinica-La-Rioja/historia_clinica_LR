package net.pladema.reports.service.impl;

import ar.lamansys.sgh.shared.domain.annex.SharedAppointmentAnnexPdfReportResponse;
import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.featureflags.application.FeatureFlagsService;
import ar.lamansys.sgx.shared.files.pdf.PdfService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.reports.controller.dto.AnnexIIDto;
import net.pladema.reports.controller.mapper.ReportsMapper;
import net.pladema.reports.domain.AnnexIIParametersBo;
import net.pladema.reports.service.AnnexReportService;
import net.pladema.reports.service.domain.AnnexIIBo;
import net.pladema.reports.service.domain.AppointmentAnnexPdfReportVo;

import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;

import ar.lamansys.sgh.shared.application.annex.SharedAppointmentAnnexPdfReportService;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppointmentAnnexPdfReportServiceImpl implements SharedAppointmentAnnexPdfReportService {

	private final AnnexReportService annexReportService;
	private final ReportsMapper reportsMapper;
	private final FeatureFlagsService featureFlagsService;
	private final PdfService pdfService;

	@Override
	public SharedAppointmentAnnexPdfReportResponse run(Integer appointmentId) {
		log.debug("Input parameter -> appointmentId {}", appointmentId);
		ZonedDateTime now = ZonedDateTime.now(ZoneId.of(JacksonDateFormatConfig.ZONE_ID));
		AnnexIIBo reportDataBo = annexReportService.getAppointmentData(new AnnexIIParametersBo(appointmentId, null));
		AnnexIIDto reportDataDto = reportsMapper.toAnexoIIDto(reportDataBo);
		reportDataDto.setRnos(reportDataBo.getRnos());
		Map<String, Object> context = annexReportService.createAppointmentContext(reportDataDto);
		setFlavor(context);

		log.debug("Output -> {}", reportDataDto);

		var pdf = pdfService.generate("annex_report", context);
		var filename = annexReportService.createConsultationFileName(appointmentId.longValue(), now);

		return new AppointmentAnnexPdfReportVo(pdf, filename);
	}

	private void setFlavor(Map<String, Object> context) {
		if (featureFlagsService.isOn(AppFeature.HABILITAR_ANEXO_II_MENDOZA))
			context.put("flavor", "mdz");
		else
			context.put("flavor", "pba");
	}
}
