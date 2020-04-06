package net.pladema.permissions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.pladema.permissions.repository.entity.Permission;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, Short>{

}
