package net.pladema.establishment.controller;

import net.pladema.imagenetwork.derivedstudies.repository.MoveStudiesRepository;
import net.pladema.imagenetwork.derivedstudies.repository.entity.MoveStudies;
import net.pladema.scheduledjobs.jobs.MoveStudiesJob;
import net.pladema.sgx.backoffice.rest.AbstractBackofficeController;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import java.util.List;

@RestController
@RequestMapping("backoffice/movestudies")
public class BackofficeMoveStudiesController extends AbstractBackofficeController<MoveStudies, Integer> {

	private final MoveStudiesRepository repository;

	public BackofficeMoveStudiesController(MoveStudiesRepository repository) {
		super(repository);
		this.repository = repository;
	}

	@Override
	public Page<MoveStudies> getList(Pageable pageable, MoveStudies entity) {
		List<MoveStudies> list = repository.listFailed();
		return new PageImpl<>(list, pageable, list.size());
	}

	@Override
	public MoveStudies update(Integer id,@Valid @RequestBody MoveStudies entity) {
		if(MoveStudiesJob.PENDING.equals(entity.getStatus())){
			entity.setAttempsNumber(0);
		}
		return super.update(id, entity);

	}

}

