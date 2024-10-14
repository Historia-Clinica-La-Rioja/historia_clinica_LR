package net.pladema.imagenetwork.domain;

import java.util.Set;

import lombok.AllArgsConstructor;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@AllArgsConstructor
@ToString
public class PacsListBo {

	public static final String SCHEMA = "https";
	public static final String CONTEXT = "imagenetwork";

	private Set<PacsBo> pacs;

	public final boolean isAvailableInPACS() {
		return !pacs.isEmpty();
	}
}
