package net.pladema.establishment.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.pladema.establishment.repository.BedCategoryRepository;
import net.pladema.establishment.repository.entity.BedCategory;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

@RestController
@RequestMapping("backoffice/bedcategories")
public class BackofficeBedCategoryController extends AbstractBackofficeController<BedCategory, Short> {

	public BackofficeBedCategoryController(BedCategoryRepository repository) {
		super(repository);
	}

}
