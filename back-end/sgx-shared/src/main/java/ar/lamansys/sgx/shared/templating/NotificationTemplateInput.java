package ar.lamansys.sgx.shared.templating;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NotificationTemplateInput<T> {
	public final String templateId;
	public final T args;
	public final AppFeature feature;

	public NotificationTemplateInput<T> withPrefixId(String prefix) {
		return new NotificationTemplateInput<>(
				prefix + this.templateId,
				this.args,
				this.feature
		);
	}

}
