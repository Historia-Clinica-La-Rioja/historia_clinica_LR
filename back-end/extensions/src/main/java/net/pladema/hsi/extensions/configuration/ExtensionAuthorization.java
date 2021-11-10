package net.pladema.hsi.extensions.configuration;

public interface ExtensionAuthorization {
	boolean isSystemMenuAllowed(String menuId);
	boolean isInstitutionMenuAllowed(String menuId, Integer institutionId);
}
