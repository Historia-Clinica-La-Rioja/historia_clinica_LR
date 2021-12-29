package net.pladema.flavor.service.impl;

import ar.lamansys.sgx.shared.flavor.FlavorBo;
import ar.lamansys.sgx.shared.flavor.InitialFeatureStatesStrategy;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

class InitialFeatureStatesStrategyTest {

	@Test
	void forFlavor_checkAllFlavors() {
		// Aseguramos que para todos los flavors hay una estrategia de FF
		Assertions.assertThatCode(
				() -> Arrays.stream(FlavorBo.values()).forEach(InitialFeatureStatesStrategy::forFlavor)
		).doesNotThrowAnyException();
	}

}