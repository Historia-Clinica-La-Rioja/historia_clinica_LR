package net.pladema.flavor.service.impl;

import java.util.Arrays;

import ar.lamansys.sgx.shared.flavor.InitialFeatureStatesStrategy;
import org.assertj.core.api.Assertions;
import org.junit.Test;

import ar.lamansys.sgx.shared.flavor.FlavorBo;

public class InitialFeatureStatesStrategyTest {

	@Test
	public void forFlavor_checkAllFlavors() {
		// Aseguramos que para todos los flavors hay una estrategia de FF
		Assertions.assertThatCode(
				() -> Arrays.stream(FlavorBo.values()).forEach(InitialFeatureStatesStrategy::forFlavor)
		).doesNotThrowAnyException();
	}

}