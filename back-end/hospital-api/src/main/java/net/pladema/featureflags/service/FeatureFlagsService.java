package net.pladema.featureflags.service;

import net.pladema.featureflags.service.domain.FlavorBo;
import net.pladema.featureflags.service.domain.FeatureFlagBo;

import java.util.List;

public interface FeatureFlagsService {

    List<FeatureFlagBo> getOnlyPublic();

	List<FeatureFlagBo> getAll();

    List<FeatureFlagBo> reset(FlavorBo flavor);

    boolean changeState(String key, boolean state);

    boolean isOn(String name);
}
