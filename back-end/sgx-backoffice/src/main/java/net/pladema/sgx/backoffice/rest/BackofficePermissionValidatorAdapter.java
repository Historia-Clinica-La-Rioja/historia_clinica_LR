package net.pladema.sgx.backoffice.rest;

import net.pladema.sgx.backoffice.exceptions.PermissionDeniedException;
import net.pladema.sgx.backoffice.permissions.BackofficePermissionValidator;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BackofficePermissionValidatorAdapter<E, I> implements BackofficePermissionValidator<E, I> {
	private final Set<HttpMethod> methodsAccepted;


	public BackofficePermissionValidatorAdapter(HttpMethod... methodsEnabled) {
		this.methodsAccepted = Stream.of(methodsEnabled).collect(Collectors.toSet());
	}

	public BackofficePermissionValidatorAdapter() {
		this(
				HttpMethod.GET,
				HttpMethod.POST,
				HttpMethod.PUT,
				HttpMethod.DELETE
		);
	}

	private void assertMethodAccepted(HttpMethod method) {
		if (!methodsAccepted.contains(method)) {
			throw new PermissionDeniedException(String.format("No se permite %s", method));
		}
	}

	@Override
	public void assertGetList(E entity) {
		assertMethodAccepted(HttpMethod.GET);
	}

	@Override
	public List<I> filterIdsByPermission(List<I> ids) {
		assertMethodAccepted(HttpMethod.GET);
		return ids;
	}

	@Override
	public void assertGetOne(I id) {
		assertMethodAccepted(HttpMethod.GET);
	}

	@Override
	public void assertCreate(E entity) {
		assertMethodAccepted(HttpMethod.POST);
	}

	@Override
	public void assertUpdate(I id, E entity) {
		assertMethodAccepted(HttpMethod.PUT);
	}

	@Override
	public void assertDelete(I id) {
		assertMethodAccepted(HttpMethod.DELETE);
	}

	@Override
	public ItemsAllowed itemsAllowedToList(E entity) {
		assertMethodAccepted(HttpMethod.GET);
		return new ItemsAllowed<>(true, new ArrayList<>());
	}

	@Override
	public ItemsAllowed itemsAllowedToList() {
		assertMethodAccepted(HttpMethod.GET);
		return new ItemsAllowed<>(true, new ArrayList<>());
	}
}
