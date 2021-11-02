package net.pladema.sgx.backoffice.permissions;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import net.pladema.sgx.backoffice.BOMethod;
import net.pladema.sgx.backoffice.permissions.impl.BackofficePermissionByActionValidator;

public class BackofficePermissionBuilder {
	private static final Supplier<Boolean> trueSupplier = () -> true;

	public static <E, I> BackofficePermissionValidator<E, I> permitAll() {
		return new BackofficePermissionByActionValidator(Collections.EMPTY_MAP, () -> true);
	}

	public static <E, I> BackofficePermissionValidator<E, I> permitOnly(BOMethod... methods) {
		Map<BOMethod, Supplier<Boolean>> isAllowedByAction = Arrays.stream(methods)
				.collect(Collectors.toMap(Function.identity(), o -> trueSupplier));
		return new BackofficePermissionByActionValidator(isAllowedByAction, () -> false);
	}

	public static <E, I> BackofficePermissionValidator<E, I> permitIf(Supplier<Boolean> supplier) {
		return new BackofficePermissionByActionValidator(Collections.EMPTY_MAP, supplier);
	}
}
