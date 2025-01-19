package net.pladema.report.application.sendreport;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import net.pladema.report.domain.SendUsageReportBo;
import net.pladema.report.domain.exceptions.SaveReportException;
import net.pladema.report.service.UseReportService;

@AllArgsConstructor
@Service
public class SendUsageReport {
	private final UseReportService useReportService;

	public SendUsageReportBo run() throws SaveReportException {
		try {
			useReportService.execute();
			return new SendUsageReportBo(
					HttpStatus.ACCEPTED.getReasonPhrase() // fue enviado
			);
		} catch (RuntimeException e) {
			throw new SaveReportException(e.getMessage());
		}

	}
}
