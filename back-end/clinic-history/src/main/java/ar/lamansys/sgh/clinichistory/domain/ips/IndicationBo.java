package ar.lamansys.sgh.clinichistory.domain.ips;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import ar.lamansys.sgx.shared.dates.configuration.JacksonDateFormatConfig;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IndicationBo {

	private Integer id;

	private Integer patientId;

	private Short typeId;

	private Short statusId;

	private Integer createdBy;

	@JsonFormat(pattern = JacksonDateFormatConfig.DATE_TIME_FORMAT)
	private LocalDateTime indicationDate;

}
