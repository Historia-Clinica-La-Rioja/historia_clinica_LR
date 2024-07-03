package ar.lamansys.sgx.shared.notifications.templating.engine;

import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

import ar.lamansys.sgx.shared.notifications.domain.RecipientBo;
import ar.lamansys.sgx.shared.notifications.templating.INotificationTemplateEngine;
import ar.lamansys.sgx.shared.notifications.templating.NotificationTemplateInput;
import ar.lamansys.sgx.shared.notifications.templating.domain.NotificationContext;
import ar.lamansys.sgx.shared.notifications.templating.domain.NotificationEnv;
import ar.lamansys.sgx.shared.templating.exceptions.TemplateException;

public abstract class NotificationTemplateEngine<T> implements INotificationTemplateEngine<T> {
	private final Supplier<NotificationEnv> environmentSupplier;

	protected NotificationTemplateEngine(Supplier<NotificationEnv> environmentSupplier) {
		this.environmentSupplier = environmentSupplier;
	}

	public T process(RecipientBo recipient, NotificationTemplateInput<?> notificationTemplateInput) throws TemplateException {
		try {
			var context = buildContext(recipient, notificationTemplateInput.args, environmentSupplier.get());
			return processImpl(notificationTemplateInput.templateId, context);
		} catch (TemplateException e) {
			throw e;
		} catch (Exception e) {
			throw new TemplateException(e.getMessage(), e);
		}
	}

	protected abstract T processImpl(String templateId, NotificationContext context) throws TemplateException;

	private static <U> NotificationContext buildContext(RecipientBo recipient, U args, NotificationEnv env) {
		Map<String, Object> variables = Map.of(
				"recipient", recipient,
				"args", args,
				"env", env
		);
		return new NotificationContext(Locale.getDefault(), variables);
	}
}
