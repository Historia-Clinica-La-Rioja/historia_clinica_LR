package net.pladema.establishment.controller;

import net.pladema.imagenetwork.derivedstudies.repository.MoveStudiesRepository;
import net.pladema.imagenetwork.derivedstudies.repository.entity.AllMoveStudiesView;
import net.pladema.scheduledjobs.jobs.MoveStudiesJob;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("backoffice/allmovestudies")
public class BackofficeAllMoveStudiesController extends AbstractBackofficeController<AllMoveStudiesView, Integer> {

	private final MoveStudiesRepository moveStudiesRepository;


	public BackofficeAllMoveStudiesController(BackofficeAllMoveStudiesStore store, MoveStudiesRepository moveStudiesRepository) {
		super(store);
		this.moveStudiesRepository = moveStudiesRepository;
	}

	@Override
	public AllMoveStudiesView update(Integer id, @Valid @RequestBody AllMoveStudiesView entity) {

		moveStudiesRepository.updateImageId(entity.getId(), entity.getImageId());
		if(MoveStudiesJob.PENDING.equals(entity.getStatus())){
			moveStudiesRepository.updateStatusandResult(entity.getId(), entity.getStatus(), "");
			moveStudiesRepository.updateAttemps(entity.getId(), 0);
		}
		else{
			moveStudiesRepository.updateStatus(entity.getId(), entity.getStatus());
		}
		return entity;

	}

}

