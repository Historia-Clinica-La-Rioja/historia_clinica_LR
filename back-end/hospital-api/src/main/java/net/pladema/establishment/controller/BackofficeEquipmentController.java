package net.pladema.establishment.controller;

import net.pladema.establishment.repository.EquipmentRepository;
import net.pladema.establishment.repository.entity.Equipment;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("backoffice/equipment")
public class BackofficeEquipmentController extends AbstractBackofficeController<Equipment, Integer> {

	private final EquipmentRepository repository;

	public BackofficeEquipmentController(EquipmentRepository repository) {
		super(repository);
		this.repository = repository;
	}

	@Override
	public Equipment create(@Valid @RequestBody Equipment entity) {
		if (entity.getCreateId() == null){
			entity.setCreateId(false);
		}
		return super.create(entity);

	}
}

