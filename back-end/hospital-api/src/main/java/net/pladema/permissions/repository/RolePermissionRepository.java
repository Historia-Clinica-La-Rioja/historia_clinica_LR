package net.pladema.permissions.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import net.pladema.permissions.repository.entity.RolePermission;
import net.pladema.permissions.repository.entity.RolePermissionPK;

@Repository
public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionPK>{

}
