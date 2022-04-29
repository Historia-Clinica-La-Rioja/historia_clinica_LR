package net.pladema.establishment.controller;

import net.pladema.establishment.repository.MedicalCoveragePlanRepository;
import net.pladema.establishment.repository.entity.MedicalCoveragePlan;
import net.pladema.sgx.backoffice.repository.BackofficeStore;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BackofficeMedicalCoveragePlanStore implements BackofficeStore<MedicalCoveragePlan, Integer> {

    private final MedicalCoveragePlanRepository repository;

    public BackofficeMedicalCoveragePlanStore(MedicalCoveragePlanRepository repository) {
        this.repository = repository;
    }

    @Override
    public Page<MedicalCoveragePlan> findAll(MedicalCoveragePlan entity, Pageable pageable) {
        List<MedicalCoveragePlan> result = this.repository.findAllActiveByMedicalCoverageId(entity.getMedicalCoverageId());
        return new PageImpl<>(result, pageable, result.size());
    }

    @Override
    public List<MedicalCoveragePlan> findAll() {
        List<MedicalCoveragePlan> result = this.repository.findAll();
        return result;
    }

    @Override
    public List<MedicalCoveragePlan> findAllById(List<Integer> ids) {
        return this.repository.findAllById(ids);
    }

    @Override
    public Optional<MedicalCoveragePlan> findById(Integer id) {
        return this.repository.findById(id);
    }

    @Override
    public MedicalCoveragePlan save(MedicalCoveragePlan entity) {
		Optional<MedicalCoveragePlan> plan = this.repository.findByIdAndPlan(entity.getMedicalCoverageId(),entity.getPlan());
		if(plan.isPresent()&&plan.get().isDeleted()){
			entity = plan.get();
			entity.setDeleted(false);
		}
		return this.repository.save(entity);
    }


    @Override
    public void deleteById(Integer id) {
        MedicalCoveragePlan medicalCoveragePlan = this.repository.findById(id).get();
        this.repository.deleteById(medicalCoveragePlan.getId());
    }

    @Override
    public Example<MedicalCoveragePlan> buildExample(MedicalCoveragePlan entity) {
        return Example.of(entity);
    }
}
