package net.pladema.establishment.controller;

import net.pladema.establishment.repository.PrivateHealthInsurancePlanRepository;
import net.pladema.establishment.repository.entity.PrivateHealthInsurancePlan;
import net.pladema.sgx.backoffice.repository.BackofficeStore;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BackofficePrivateHealthInsurancePlanStore implements BackofficeStore<PrivateHealthInsurancePlan, Integer> {

    private final PrivateHealthInsurancePlanRepository repository;

    public BackofficePrivateHealthInsurancePlanStore(PrivateHealthInsurancePlanRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<PrivateHealthInsurancePlan> findAll(PrivateHealthInsurancePlan entity, Pageable pageable) {
        List<PrivateHealthInsurancePlan> result = this.repository.findByPrivateHealthInsuranceId(entity.getPrivateHealthInsuranceId());
        return new PageImpl<>(result, pageable, result.size());
    }

    @Override
    public List<PrivateHealthInsurancePlan> findAll() {
        List<PrivateHealthInsurancePlan> result = this.repository.findAll();
        return result;
    }

    @Override
    public List<PrivateHealthInsurancePlan> findAllById(List<Integer> ids) {
        return this.repository.findAllById(ids);
    }

    @Override
    public Optional<PrivateHealthInsurancePlan> findById(Integer id) {
        return this.repository.findById(id);
    }

    @Override
    public PrivateHealthInsurancePlan save(PrivateHealthInsurancePlan entity) {
        PrivateHealthInsurancePlan privateHealthInsurancePlan = this.repository.findByIdAndPlan(entity.getPrivateHealthInsuranceId(), entity.getPlan());
        if (privateHealthInsurancePlan != null) {
            return update(privateHealthInsurancePlan);
        }
        return create(entity);
    }

    public PrivateHealthInsurancePlan update(PrivateHealthInsurancePlan entity) {
        return this.repository.save(entity);
    }

    public PrivateHealthInsurancePlan create(PrivateHealthInsurancePlan entity) {
        return this.repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        PrivateHealthInsurancePlan privateHealthInsurancePlan = this.repository.findById(id).get();
        this.repository.deleteById(privateHealthInsurancePlan.getId());
    }

    @Override
    public Example<PrivateHealthInsurancePlan> buildExample(PrivateHealthInsurancePlan entity) {
        return Example.of(entity);
    }
}
