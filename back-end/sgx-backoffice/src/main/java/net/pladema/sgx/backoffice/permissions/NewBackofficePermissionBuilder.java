package net.pladema.sgx.backoffice.permissions;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import net.pladema.sgx.backoffice.BOMethod;
import net.pladema.sgx.backoffice.permissions.impl.NewBackofficePermissionByActionValidator;

public class NewBackofficePermissionBuilder {

	private static final Map<BOMethod, Supplier<Boolean>> EMPTY_ACTIONS = Collections.emptyMap();
	private static final Supplier<Boolean> trueSupplier = () -> true;

	public static <E, I> NewBackofficePermissionValidator<E, I> permitAll() {
		return new NewBackofficePermissionByActionValidator<>(EMPTY_ACTIONS, () -> true);
	}

	public static <E, I> NewBackofficePermissionValidator<E, I> permitOnly(BOMethod... methods) {
		Map<BOMethod, Supplier<Boolean>> isAllowedByAction = Arrays.stream(methods)
				.collect(Collectors.toMap(Function.identity(), o -> trueSupplier));
		return new NewBackofficePermissionByActionValidator<>(isAllowedByAction, () -> false);
	}

	public static <E, I> NewBackofficePermissionValidator<E, I> permitIf(Supplier<Boolean> supplier) {
		return new NewBackofficePermissionByActionValidator<>(EMPTY_ACTIONS, supplier);
	}
}
