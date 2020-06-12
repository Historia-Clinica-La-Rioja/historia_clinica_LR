package net.pladema.flavor.service.impl;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import net.pladema.featureflags.service.domain.FlavorBo;
import net.pladema.flavor.service.FlavorService;

@Service
public class FlavorServiceImpl implements FlavorService {
	private final FlavorBo flavor;

	public FlavorServiceImpl(@Value("${app.flavor:minsal}") String flavor) {
		this.flavor = FlavorBo.getEnum(flavor);
	}

	@Override
	public FlavorBo getFlavor() {
		return this.flavor;
	}

}
