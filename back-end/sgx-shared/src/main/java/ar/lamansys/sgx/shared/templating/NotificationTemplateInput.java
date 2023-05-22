package ar.lamansys.sgx.shared.templating;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.springframework.core.io.ByteArrayResource;

import ar.lamansys.sgx.shared.featureflags.AppFeature;
import ar.lamansys.sgx.shared.filestorage.infrastructure.input.rest.StoredFileBo;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class NotificationTemplateInput<T> {
	public final String templateId;
	public final T args;
	public final AppFeature feature;
	public final List<StoredFileBo> attachments;
	public String subject;

	public NotificationTemplateInput(String templateId, T args, AppFeature feature) {
		this.templateId = templateId;
		this.args = args;
		this.feature = feature;
		attachments = Collections.emptyList();
	}

	public NotificationTemplateInput<T> withPrefixId(String prefix) {
		return new NotificationTemplateInput<>(
				prefix + this.templateId,
				this.args,
				this.feature
		);
	}

}
