package ar.lamansys.sgx.shared.templating;

import java.util.HashMap;

import org.springframework.core.io.ByteArrayResource;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NotificationTemplateInput<T> {
	public final String templateId;
	public final T args;
	public final AppFeature feature;
	public final HashMap<String, ByteArrayResource> attachments;

	public NotificationTemplateInput(String templateId, T args, AppFeature feature) {
		this.templateId = templateId;
		this.args = args;
		this.feature = feature;
		attachments = new HashMap<>();
	}

	public NotificationTemplateInput<T> withPrefixId(String prefix) {
		return new NotificationTemplateInput<>(
				prefix + this.templateId,
				this.args,
				this.feature
		);
	}

}
