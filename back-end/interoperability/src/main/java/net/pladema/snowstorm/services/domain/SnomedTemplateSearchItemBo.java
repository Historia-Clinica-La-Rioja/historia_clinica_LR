package net.pladema.snowstorm.services.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@AllArgsConstructor
@ToString
public class SnomedTemplateSearchItemBo {

	private Integer groupId;

	private String description;

	private List<SnomedSearchItemBo> concepts;

}
