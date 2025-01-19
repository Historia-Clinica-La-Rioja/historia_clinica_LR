package net.pladema.violencereport.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CoordinationActionBo {

	private Boolean within;

	private List<Short> organizations;

	private String other;

}
