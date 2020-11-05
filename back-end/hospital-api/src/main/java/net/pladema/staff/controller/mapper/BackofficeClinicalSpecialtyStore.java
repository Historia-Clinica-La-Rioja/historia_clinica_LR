package net.pladema.staff.controller.mapper;

import net.pladema.sgx.backoffice.repository.BackofficeStore;
import net.pladema.staff.repository.ClinicalSpecialtyRepository;
import net.pladema.staff.repository.entity.ClinicalSpecialty;
import net.pladema.staff.repository.entity.ClinicalSpecialtyType;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class BackofficeClinicalSpecialtyStore implements BackofficeStore<ClinicalSpecialty, Integer> {


    private final ClinicalSpecialtyRepository repository;

    public BackofficeClinicalSpecialtyStore(ClinicalSpecialtyRepository repository){
        super();
        this.repository=repository;
    }

    @Override
    public Page<ClinicalSpecialty> findAll(ClinicalSpecialty clinicalSpecialty, Pageable pageable) {
        clinicalSpecialty.setClinicalSpecialtyTypeId(ClinicalSpecialtyType.Specialty);
        if(clinicalSpecialty.withoutName())
            clinicalSpecialty.setName(null);
        return repository.findAll(
                buildExample(clinicalSpecialty),
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        Sort.by(Sort.Direction.ASC, "name")
                )
        );
    }

    @Override
    public List<ClinicalSpecialty> findAll() {
        ClinicalSpecialty specialties = new ClinicalSpecialty();
        specialties.setClinicalSpecialtyTypeId(ClinicalSpecialtyType.Specialty);
        return repository.findAll(Example.of(specialties), Sort.by(Sort.Direction.ASC, "name"));
    }

    @Override
    public List<ClinicalSpecialty> findAllById(List<Integer> ids) {
        List<ClinicalSpecialty> specialties = repository.findAllById(ids);
        specialties.forEach(ClinicalSpecialty::fixSpecialtyType);
        specialties.sort(Comparator.comparing(ClinicalSpecialty::getName, String::compareTo));
        return specialties;
    }

    @Override
    public Optional<ClinicalSpecialty> findById(Integer id) {
        Optional<ClinicalSpecialty> clinicalSpecialty = repository.findById(id);
        clinicalSpecialty.ifPresent(ClinicalSpecialty::fixSpecialtyType);
        return clinicalSpecialty;
    }

    @Override
    public ClinicalSpecialty save(ClinicalSpecialty entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public Example<ClinicalSpecialty> buildExample(ClinicalSpecialty entity) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatcher::contains)
                .withMatcher("sctidCode", ExampleMatcher.GenericPropertyMatcher::contains);
        return Example.of(entity, matcher);
    }
}
