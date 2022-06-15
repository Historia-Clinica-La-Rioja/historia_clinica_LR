package net.pladema.staff.controller.mapper;

import net.pladema.establishment.repository.ClinicalServiceSectorRepository;
import net.pladema.establishment.repository.VClinicalServiceSectorRepository;
import net.pladema.establishment.repository.entity.ClinicalSpecialtySector;
import net.pladema.establishment.repository.entity.VClinicalServiceSector;
import net.pladema.sgx.backoffice.repository.BackofficeStore;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BackofficeClinicalServiceSectorStore implements BackofficeStore<ClinicalSpecialtySector, Integer> {

	private final ClinicalServiceSectorRepository clinicalServiceSectorRepository;

	private final VClinicalServiceSectorRepository vClinicalServiceSectorRepository;

	public BackofficeClinicalServiceSectorStore(ClinicalServiceSectorRepository clinicalServiceSectorRepository,
												VClinicalServiceSectorRepository vClinicalServiceSectorRepository) {
		this.clinicalServiceSectorRepository = clinicalServiceSectorRepository;
		this.vClinicalServiceSectorRepository = vClinicalServiceSectorRepository;
	}

	@Override
	public Page<ClinicalSpecialtySector> findAll(ClinicalSpecialtySector example, Pageable pageable) {
		Example<VClinicalServiceSector> entity = buildExample(new VClinicalServiceSector(example));
		return vClinicalServiceSectorRepository.findAll(entity, pageable).map(VClinicalServiceSector::parseToClinicalSpecialtySector);
	}

	@Override
	public List<ClinicalSpecialtySector> findAll() {
		return vClinicalServiceSectorRepository.findAll().stream().map(VClinicalServiceSector::parseToClinicalSpecialtySector).collect(Collectors.toList());
	}

	@Override
	public List<ClinicalSpecialtySector> findAllById(List<Integer> ids) {
		return vClinicalServiceSectorRepository.findAllById(ids).stream().map(VClinicalServiceSector::parseToClinicalSpecialtySector).collect(Collectors.toList());
	}

	@Override
	public Optional<ClinicalSpecialtySector> findById(Integer id) {
		return clinicalServiceSectorRepository.findById(id);
	}

	@Override
	public ClinicalSpecialtySector save(ClinicalSpecialtySector entity) {
		return clinicalServiceSectorRepository.save(entity);
	}

	@Override
	public void deleteById(Integer id) {
		clinicalServiceSectorRepository.deleteById(id);
	}

	@Override
	public Example<ClinicalSpecialtySector> buildExample(ClinicalSpecialtySector entity) {
		return null;
	}

	public Example<VClinicalServiceSector> buildExample(VClinicalServiceSector entity) {
		ExampleMatcher matcher = ExampleMatcher.matching().withMatcher("description", x -> x.ignoreCase().contains());
		return Example.of(entity, matcher);
	}

}
