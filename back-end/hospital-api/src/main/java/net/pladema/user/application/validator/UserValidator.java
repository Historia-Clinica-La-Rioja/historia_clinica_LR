package net.pladema.user.application.validator;


import net.pladema.sgx.exceptions.PermissionDeniedException;

public class UserValidator {
    private final UserAuthoritiesValidator authoritiesValidator;

    public UserValidator(UserAuthoritiesValidator authoritiesValidator) {
        this.authoritiesValidator = authoritiesValidator;
    }

    public void assertUpdate(Integer userId) {
        if (authoritiesValidator.isLoggedUserId(userId)) {
            throw new PermissionDeniedException("No cuenta con suficientes privilegios");
        }
        authoritiesValidator.assertLoggedUserOutrank(userId);
    }
}
