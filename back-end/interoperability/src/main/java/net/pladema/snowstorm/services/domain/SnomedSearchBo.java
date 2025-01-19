package net.pladema.snowstorm.services.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.snowstorm.domain.SnomedSearchItemBo;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class SnomedSearchBo {

    private List<SnomedSearchItemBo> items;

    private Integer totalMatches;

}
