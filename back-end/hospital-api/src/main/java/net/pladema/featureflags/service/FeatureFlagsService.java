package net.pladema.featureflags.service;

public interface FeatureFlagsService {

    boolean isOn(String name);
}
