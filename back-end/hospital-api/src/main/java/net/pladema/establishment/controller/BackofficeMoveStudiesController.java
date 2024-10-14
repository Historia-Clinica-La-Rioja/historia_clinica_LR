package net.pladema.establishment.controller;

import net.pladema.imagenetwork.derivedstudies.repository.entity.MoveStudies;
import net.pladema.scheduledjobs.jobs.MoveStudiesJob;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("backoffice/movestudies")
public class BackofficeMoveStudiesController extends AbstractBackofficeController<MoveStudies, Integer> {



	public BackofficeMoveStudiesController(BackofficeMoveStudiesStore store ) {
		super(store);
	}

	@Override
	public MoveStudies update(Integer id,@Valid @RequestBody MoveStudies entity) {
		if(MoveStudiesJob.PENDING.equals(entity.getStatus())){
			entity.setAttempsNumber(0);
		}
		return super.update(id, entity);

	}

}

