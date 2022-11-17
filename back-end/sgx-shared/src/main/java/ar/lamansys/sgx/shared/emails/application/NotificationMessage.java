package ar.lamansys.sgx.shared.emails.application;

import java.util.Optional;

public interface NotificationMessage {
	Optional<EmailNotificationMessage> forEmail();
}
