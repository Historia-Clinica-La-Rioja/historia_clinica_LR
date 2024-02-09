package net.pladema.establishment.controller;


import lombok.RequiredArgsConstructor;
import net.pladema.imagenetwork.derivedstudies.repository.MoveStudiesRepository;
import net.pladema.imagenetwork.derivedstudies.repository.entity.MoveStudies;
import net.pladema.sgx.backoffice.repository.BackofficeStore;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BackofficeMoveStudiesStore implements BackofficeStore<MoveStudies, Integer> {


	private final MoveStudiesRepository moveStudiesRepository;

	@Override
	public Page<MoveStudies> findAll(MoveStudies entity, Pageable pageable) {
		String filterImageId = entity.getImageId();
		Page<MoveStudies> moveStudiesList = (filterImageId != null) ?
				moveStudiesRepository.findByImageId("%" + filterImageId + "%",pageable) : moveStudiesRepository.findAll(pageable);
		return moveStudiesList;
	}

	@Override
	public List<MoveStudies> findAll() {

		return moveStudiesRepository.findAll();
	}

	@Override
	public List<MoveStudies> findAllById(List<Integer> ids) {
		return moveStudiesRepository.findAllById(ids);
	}

	@Override
	public Optional<MoveStudies> findById(Integer id) {
		return moveStudiesRepository.findById(id);
	}

	@Override
	public MoveStudies save(MoveStudies entity) {
		return moveStudiesRepository.save(entity);
	}

	@Override
	public void deleteById(Integer id) {}


	@Override
	public Example<MoveStudies> buildExample(MoveStudies entity) {
		ExampleMatcher customExampleMatcher = ExampleMatcher.matching();
		return Example.of(entity, customExampleMatcher);
	}

}
