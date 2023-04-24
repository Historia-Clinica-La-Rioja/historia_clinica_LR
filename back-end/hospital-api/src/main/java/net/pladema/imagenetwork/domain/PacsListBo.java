package net.pladema.imagenetwork.domain;

import java.net.URI;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.web.util.UriComponentsBuilder;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.establishment.repository.entity.PacServer;

@Getter
@Setter
@ToString
public class PacsListBo {
	private Set<URI> pacs;

	private static final String SCHEMA = "https";

	public PacsListBo(List<PacServer> pacServers) {
		this.pacs = getURIs(pacServers);
	}

	private static Set<URI> getURIs(List<PacServer> pacs) {
		return pacs.stream()
				.map(pacServer -> UriComponentsBuilder.newInstance()
						.scheme(SCHEMA)
						.host(pacServer.getDomain())
						.build()
						.toUri())
				.collect(Collectors.toSet());
	}
}
