package ar.lamansys.sgh.publicapi.domain;

import lombok.AllArgsConstructor;

import java.util.List;

@AllArgsConstructor
public class DiagnosesBo {
	private SnomedBo main;
	private List<SnomedBo> others;
}
