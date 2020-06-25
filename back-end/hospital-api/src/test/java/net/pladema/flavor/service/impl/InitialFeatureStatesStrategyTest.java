package net.pladema.flavor.service.impl;

import java.util.Arrays;

import org.assertj.core.api.Assertions;
import org.junit.Test;

import net.pladema.featureflags.service.domain.FlavorBo;

public class InitialFeatureStatesStrategyTest {

	@Test
	public void forFlavor_checkAllFlavors() {
		// Aseguramos que para todos los flavors hay una estrategia de FF
		Assertions.assertThatCode(
				() -> Arrays.stream(FlavorBo.values()).forEach(InitialFeatureStatesStrategy::forFlavor)
		).doesNotThrowAnyException();
	}

}