package net.pladema.snowstorm.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class SnomedSearchBo {

	private List<SnomedSearchItemBo> items;

	private Integer totalMatches;

}
