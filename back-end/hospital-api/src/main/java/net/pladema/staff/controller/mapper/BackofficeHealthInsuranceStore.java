package net.pladema.staff.controller.mapper;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import net.pladema.person.repository.HealthInsuranceRepository;
import net.pladema.person.repository.entity.HealthInsurance;
import net.pladema.sgx.backoffice.repository.BackofficeStore;

@Service
public class BackofficeHealthInsuranceStore implements BackofficeStore<HealthInsurance, Integer> {


    private final HealthInsuranceRepository repository;

    public BackofficeHealthInsuranceStore(HealthInsuranceRepository repository){
        super();
        this.repository=repository;
    }

    @Override
    public Page<HealthInsurance> findAll(HealthInsurance healthInsurance, Pageable pageable) {
        return repository.findAll(
                buildExample(healthInsurance),
                PageRequest.of(
                        pageable.getPageNumber(),
                        pageable.getPageSize(),
                        Sort.by(Sort.Direction.ASC, "name")
                )
        );
    }

    @Override
    public List<HealthInsurance> findAll() {
        HealthInsurance healthInsurance = new HealthInsurance();
        return repository.findAll(Example.of(healthInsurance), Sort.by(Sort.Direction.ASC, "name"));
    }

    @Override
    public List<HealthInsurance> findAllById(List<Integer> ids) {
        List<HealthInsurance> healthInsurance = repository.findAllById(ids);
        healthInsurance.sort(Comparator.comparing(HealthInsurance::getName, String::compareTo));
        return healthInsurance;
    }

    @Override
    public Optional<HealthInsurance> findById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public HealthInsurance save(HealthInsurance entity) {
        return repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public Example<HealthInsurance> buildExample(HealthInsurance entity) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatcher::contains);
        return Example.of(entity, matcher);
    }
}

