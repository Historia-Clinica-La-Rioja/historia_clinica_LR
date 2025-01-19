package ar.lamansys.sgh.publicapi.userinformation.infrastructure.input.service;

public interface UserInformationPublicApiPermission {
	boolean canAccess();

	boolean canAccessUserInfoFromToken();

	boolean canAccessUserAuthoritiesFromToken();

	boolean canFetchProfessionalLicenses();
}
