package ar.lamansys.sgh.publicapi.domain;

import ar.lamansys.sgh.publicapi.activities.domain.SnomedBo;
import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class DiagnosesBo {
	private SnomedBo main;
	private List<SnomedBo> others;
}
