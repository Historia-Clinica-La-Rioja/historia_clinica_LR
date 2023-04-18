package net.pladema.imagenetwork.domain;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.establishment.repository.entity.PacServer;
import net.pladema.imagenetwork.infrastructure.output.entity.StudyInformation;

@Getter
@Setter
@ToString
public class PacsListBo {
	private Set<URI> pacs;

	private static final String SCHEMA = "https";

	public PacsListBo(StudyInformation studyInformation) {
		this.pacs = getURIs(studyInformation.getPacs());
	}

	private static Set<URI> getURIs(Set<PacServer> pacs) {
		return pacs.stream()
				.map(pac -> {
					try {
						return new URI(SCHEMA, pac.getDomain(), "");
					} catch (URISyntaxException e) {
						throw new RuntimeException(e);
					}
				})
				.collect(Collectors.toSet());
	}
}
