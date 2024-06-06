package net.pladema.establishment.controller;


import lombok.RequiredArgsConstructor;
import net.pladema.imagenetwork.derivedstudies.repository.AllMoveStudiesViewRepository;
import net.pladema.imagenetwork.derivedstudies.repository.MoveStudiesRepository;
import net.pladema.imagenetwork.derivedstudies.repository.entity.AllMoveStudiesView;
import net.pladema.imagenetwork.derivedstudies.repository.entity.MoveStudies;
import net.pladema.sgx.backoffice.repository.BackofficeStore;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BackofficeAllMoveStudiesStore implements BackofficeStore<AllMoveStudiesView, Integer> {


	private final AllMoveStudiesViewRepository moveStudiesRepository;

	@Override
	public Page<AllMoveStudiesView> findAll(AllMoveStudiesView entity, Pageable pageable) {

		return moveStudiesRepository.findAll(buildExample(entity), pageable);
	}

	@Override
	public List<AllMoveStudiesView> findAll() {
		return moveStudiesRepository.findAll();
	}

	@Override
	public List<AllMoveStudiesView> findAllById(List<Integer> ids) {
		return moveStudiesRepository.findAllById(ids);
	}

	@Override
	public Optional<AllMoveStudiesView> findById(Integer id) {
		return moveStudiesRepository.findById(id);
	}

	@Override
	public AllMoveStudiesView save(AllMoveStudiesView entity) {
		return moveStudiesRepository.save(entity);
	}

	@Override
	public void deleteById(Integer id) {}


	@Override
	public Example<AllMoveStudiesView> buildExample(AllMoveStudiesView entity) {
		ExampleMatcher customExampleMatcher = ExampleMatcher.matching().withMatcher("identificationNumber",
				ExampleMatcher.GenericPropertyMatcher::startsWith)
				.withMatcher("status", ExampleMatcher.GenericPropertyMatcher::startsWith)
				.withMatcher("result", x -> x.ignoreCase().contains())
				.withMatcher("imageId", x -> x.ignoreCase().contains())
				.withMatcher("lastName", x -> x.ignoreCase().contains())
				.withMatcher("firstName", x -> x.ignoreCase().contains());


		return Example.of(entity, customExampleMatcher);
	}

}
