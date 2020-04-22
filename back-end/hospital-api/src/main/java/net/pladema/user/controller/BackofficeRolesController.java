package net.pladema.user.controller;

import net.pladema.auditable.entity.Audit;
import net.pladema.permissions.repository.RoleRepository;
import net.pladema.permissions.repository.entity.Role;
import net.pladema.sgx.backoffice.repository.BackofficeRepository;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import net.pladema.sgx.backoffice.rest.BackofficeQueryAdapter;
import org.springframework.data.domain.Example;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/roles")
public class BackofficeRolesController extends AbstractBackofficeController<Role, Short> {
	public BackofficeRolesController(
			RoleRepository repository
	) {
		super(
				new BackofficeRepository(repository, new BackofficeQueryAdapter<Role>() {
					@Override
					public Example buildExample(Role entity) {
						entity.setAudit(new Audit());
						return super.buildExample(entity);
					}
				})
		);
	}
}
