package ar.lamansys.sgx.shared.notifications.application;

import java.util.Optional;

import ar.lamansys.sgx.shared.notifications.domain.RecipientBo;

public interface RecipientMapper<T> {
	Optional<RecipientBo> toRecipient(T recipientId);
}
