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
	private Set<URI> urls;

	private static final String SCHEMA = "https";
	private static final String context = "imagenetwork";
	public PacsListBo(List<PacServer> pacServers) {
		this.urls = getURIs(pacServers);
	}

	private static Set<URI> getURIs(List<PacServer> pacs) {
		return pacs.stream()
				.map(pacServer -> UriComponentsBuilder.newInstance()
						.scheme(SCHEMA)
						.host(pacServer.getDomain())
						.path(context)
						.build()
						.toUri())
				.collect(Collectors.toSet());
	}

	public final boolean isAvailableInPACS() {
		return !getUrls().isEmpty();
	}
}
