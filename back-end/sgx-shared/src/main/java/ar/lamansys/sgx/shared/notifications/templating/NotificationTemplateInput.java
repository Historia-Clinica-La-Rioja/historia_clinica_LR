package ar.lamansys.sgx.shared.notifications.templating;

import java.util.Collections;
import java.util.List;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileBo;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NotificationTemplateInput<T> {
	public final String templateId;
	public final T args;
	public final AppFeature feature;
	public final List<StoredFileBo> attachments;
	public final String subject;

	public NotificationTemplateInput(String templateId, T args, AppFeature feature) {
		this(templateId, args, feature, Collections.emptyList(), null);
	}

	public NotificationTemplateInput<T> withPrefixId(String prefix) {
		return new NotificationTemplateInput<>(
				prefix + this.templateId,
				this.args,
				this.feature,
				Collections.emptyList(),
				null
		);
	}

}
