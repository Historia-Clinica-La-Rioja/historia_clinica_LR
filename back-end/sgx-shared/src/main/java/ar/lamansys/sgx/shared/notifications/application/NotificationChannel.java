package ar.lamansys.sgx.shared.notifications.application;

import ar.lamansys.sgx.shared.notifications.domain.RecipientBo;

public interface NotificationChannel<T> {
	void send(RecipientBo recipientBo, T message);
}
