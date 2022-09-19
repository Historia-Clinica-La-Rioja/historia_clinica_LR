package net.pladema.permissions.application.ports;

import java.util.Optional;

public interface PermissionStorage {
	Optional<Integer> fetchUserIdFromToken(String token);
}
