package ar.lamansys.sgx.shared.templating;

import ar.lamansys.sgx.shared.notifications.domain.RecipientBo;
import ar.lamansys.sgx.shared.templating.exceptions.TemplateException;

public interface INotificationTemplateEngine<T> {
	T process(RecipientBo recipient, NotificationTemplateInput<?> message) throws TemplateException;
}
