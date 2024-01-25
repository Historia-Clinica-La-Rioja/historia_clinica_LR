package net.pladema.violencereport.infrastructure.input.rest.dto;

import ar.lamansys.sgh.shared.infrastructure.input.service.FilterOptionDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ViolenceReportFilterOptionDto {

	private List<FilterOptionDto> situations;

	private List<FilterOptionDto> modalities;

	private List<FilterOptionDto> types;

	private List<FilterOptionDto> institutions;

}
