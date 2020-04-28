package net.pladema.permissions.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import net.pladema.permissions.repository.entity.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role, Short> {

}
