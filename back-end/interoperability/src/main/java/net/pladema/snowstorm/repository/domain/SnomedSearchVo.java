package net.pladema.snowstorm.repository.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class SnomedSearchVo {

    private List<SnomedSearchItemVo> items;

    private Integer totalMatches;

	public SnomedSearchVo(List<SnomedSearchItemVo> items) {
		this.items = items;
	}

}
