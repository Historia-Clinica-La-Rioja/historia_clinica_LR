package net.pladema.establishment.controller;

import net.pladema.establishment.repository.DependencyRepository;
import net.pladema.establishment.repository.entity.Dependency;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("backoffice/dependencies")
public class BackofficeDependencyController extends AbstractBackofficeController<Dependency, Integer> {

    public BackofficeDependencyController(DependencyRepository dependencyRepository) { super(dependencyRepository);}

}
