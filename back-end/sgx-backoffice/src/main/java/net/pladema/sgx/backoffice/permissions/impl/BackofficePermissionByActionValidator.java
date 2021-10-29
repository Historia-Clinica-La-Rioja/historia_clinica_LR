package net.pladema.sgx.backoffice.permissions.impl;

import net.pladema.sgx.backoffice.BOMethod;
import net.pladema.sgx.backoffice.exceptions.PermissionDeniedException;
import net.pladema.sgx.backoffice.permissions.BackofficePermissionValidator;
import net.pladema.sgx.backoffice.rest.ItemsAllowed;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BackofficePermissionByActionValidator<E, I> implements BackofficePermissionValidator<E, I> {
	private final Logger logger;
	private final Map<BOMethod, Supplier<Boolean>> isAllowedByAction;
	private final Supplier<Boolean> defaultValue;
	private final Set<HttpMethod> methodsAccepted;

	public BackofficePermissionByActionValidator(
			Map<BOMethod, Supplier<Boolean>> isAllowedByAction,
			Supplier<Boolean> defaultValue,
			HttpMethod... methodsEnabled) {
		this.methodsAccepted = Stream.of(methodsEnabled).collect(Collectors.toSet());
		this.logger = LoggerFactory.getLogger(getClass());
		this.isAllowedByAction = isAllowedByAction;
		this.defaultValue = defaultValue;
	}

	@Override
	public void assertGetList(E entity) {
		assertTrue(BOMethod.GET_LIST, String.format("(filtro %s)", entity));
	}

	@Override
	public List<I> filterIdsByPermission(List<I> ids) {
		return Collections.emptyList();
	}

	private void assertTrue(BOMethod method, String message) {
		if (isAllowedByAction.getOrDefault(method, defaultValue).get()) {
			logger.debug("Se permite {} con {}", method,  message);
			return;
		}
		throw new PermissionDeniedException(
				String.format("No se permite %s con %s", method,  message)
		);
	}

	@Override
	public void assertGetOne(I id) {
		assertTrue(BOMethod.GET_ONE, String.format("(id %s)", id));
	}

	@Override
	public void assertCreate(E entity) {
		assertTrue(BOMethod.CREATE, String.format("(body %s)", entity));
	}

	@Override
	public void assertUpdate(I id, E entity) {
		assertTrue(BOMethod.UPDATE, String.format("(id %s, body %s)", id, entity));
	}

	@Override
	public void assertDelete(I id) {
		assertTrue(BOMethod.DELETE, String.format("(id %s)", id));
	}

	@Override
	public ItemsAllowed<I> itemsAllowedToList(E entity) {
		assertMethodAccepted(HttpMethod.GET);
		return new ItemsAllowed<>(true, new ArrayList<>());
	}

	@Override
	public ItemsAllowed<I> itemsAllowedToList() {
		assertMethodAccepted(HttpMethod.GET);
		return new ItemsAllowed<>(true, new ArrayList<>());
	}

	private void assertMethodAccepted(HttpMethod method) {
		if (!methodsAccepted.contains(method)) {
			throw new PermissionDeniedException(String.format("No se permite %s", method));
		}
	}
}

