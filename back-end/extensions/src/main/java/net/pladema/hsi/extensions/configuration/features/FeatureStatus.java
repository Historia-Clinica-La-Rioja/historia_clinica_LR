package net.pladema.hsi.extensions.configuration.features;

import java.util.List;
import java.util.Map;

public class FeatureStatus {
	public final String prefix;
	public final List<FeatureProperty> properties;
	public final Map<String, Object> data;

	public FeatureStatus(String prefix, List<FeatureProperty> properties, Map<String, Object> data) {
		this.prefix = prefix;
		this.properties = properties;
		this.data = data;
	}
}
