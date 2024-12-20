package net.pladema.snowstorm.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SnomedTemplateDto {

	private Integer id;

	private String description;

	private List<SnomedSearchItemDto> concepts;

}
