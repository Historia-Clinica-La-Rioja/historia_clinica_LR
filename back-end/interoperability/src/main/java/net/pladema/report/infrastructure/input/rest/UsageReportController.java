package net.pladema.report.infrastructure.input.rest;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.pladema.report.application.reportstatus.FetchUsageReportStatus;
import net.pladema.report.application.sendreport.SendUsageReport;
import net.pladema.report.domain.exceptions.SaveReportException;
import net.pladema.report.domain.exceptions.UsageReportStatusException;
import net.pladema.report.infrastructure.input.rest.dto.SendUsageReportDto;
import net.pladema.report.infrastructure.input.rest.dto.UsageReportStatusDto;

@Slf4j
@Tag(name = "Usage-Report", description = "Usage Report")
@AllArgsConstructor
@RestController
@RequestMapping("/usage-report")
public class UsageReportController {
	private final FetchUsageReportStatus usageReportStatus;
	private final SendUsageReport sendUsageReport;

	@PreAuthorize("hasAnyAuthority('ROOT')")
	@GetMapping
	public @ResponseBody UsageReportStatusDto getStatus() throws UsageReportStatusException {
		var status = usageReportStatus.run();

		return new UsageReportStatusDto("", status.enabled);
	}

	@PreAuthorize("hasAnyAuthority('ROOT')")
	@PostMapping
	public @ResponseBody SendUsageReportDto sendReport() throws SaveReportException {
		var result = sendUsageReport.run();
		return new SendUsageReportDto(
				result.code
		);
	}
}
