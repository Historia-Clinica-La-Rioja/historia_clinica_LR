package net.pladema.hsi.extensions.configuration;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExtensionAuthorizationBuilder {
	private final Supplier<Boolean> systemMenuDefault;
	private final Function<Integer, Boolean> institutionMenuDefault;
	private Map<String, Supplier<Boolean>> ruleBySystemMenu;
	private Map<String, Function<Integer, Boolean>> ruleByInstitutionMenu;

	public ExtensionAuthorizationBuilder(
			Supplier<Boolean> systemMenuDefault,
			Function<Integer, Boolean> institutionMenuDefault
	) {
		this.systemMenuDefault = systemMenuDefault;
		this.institutionMenuDefault = institutionMenuDefault;
		this.ruleBySystemMenu = new HashMap<>();
		this.ruleByInstitutionMenu = new HashMap<>();
	}

	public static ExtensionAuthorizationBuilder buildExtensionAuthorization() {
		return new ExtensionAuthorizationBuilder(() -> Boolean.FALSE, (Integer i) -> Boolean.FALSE);
	}


	public ExtensionAuthorization build() {
		return new ExtensionAuthorization() {

			@Override
			public boolean isSystemMenuAllowed(String menuId) {
				return ruleBySystemMenu.getOrDefault(menuId, systemMenuDefault).get();
			}

			@Override
			public boolean isInstitutionMenuAllowed(String menuId, Integer institutionId) {
				var menuIsAllowed = ruleByInstitutionMenu.getOrDefault(menuId, institutionMenuDefault).apply(institutionId);
				log.debug("ExtensionInstitutionMenu {} is allowed for user? {}", menuId, menuIsAllowed);
				return menuIsAllowed;
			}
		};
	}


	public ExtensionAuthorizationBuilder systemMenu(String menuId, Supplier<Boolean> isAllow) {
		ruleBySystemMenu.put(menuId, isAllow);
		return this;
	}

	public ExtensionAuthorizationBuilder isInstitutionMenuAllowed(String menuId, Function<Integer, Boolean> isAllow) {
		ruleByInstitutionMenu.put(menuId, isAllow);
		return this;
	}

	public ExtensionAuthorizationBuilder systemMenu(String[] menuIds, Supplier<Boolean> isAllow) {
		Arrays.stream(menuIds).forEach((menuId -> systemMenu(menuId, isAllow)));
		return this;
	}
}
