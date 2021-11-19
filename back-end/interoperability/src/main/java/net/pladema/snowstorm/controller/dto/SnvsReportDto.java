package net.pladema.snowstorm.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SnvsReportDto {

	private Integer id;

	private String status;

	private Short responseCode;

	private Integer sisaRegisteredId;

	private LocalDate lastUpdate;
}
