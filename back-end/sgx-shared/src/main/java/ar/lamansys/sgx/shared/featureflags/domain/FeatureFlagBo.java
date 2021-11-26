package ar.lamansys.sgx.shared.featureflags.domain;

import ar.lamansys.sgx.shared.featureflags.AppFeature;

public class FeatureFlagBo {
    public final AppFeature key;
    public final boolean active;

    public FeatureFlagBo(AppFeature key, boolean active) {
        this.key = key;
        this.active = active;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("FeatureFlagBo{");
        sb.append("key=").append(key);
        sb.append(", active=").append(active);
        sb.append('}');
        return sb.toString();
    }
}
