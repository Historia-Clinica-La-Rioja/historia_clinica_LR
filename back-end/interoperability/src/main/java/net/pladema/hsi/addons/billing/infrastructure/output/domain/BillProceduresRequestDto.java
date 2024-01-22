package net.pladema.hsi.addons.billing.infrastructure.output.domain;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BillProceduresRequestDto {
	private List<BillProceduresRequestItemDto> snomeds;
	private String medicalCoverageCuit;
	@JsonFormat(pattern = JacksonDateFormatConfig.DATE_TIME_FORMAT)
	private LocalDateTime date;

	@AllArgsConstructor
	@Getter
	public static class BillProceduresRequestItemDto {
		private String sctId;
		private String pt;
	}
}