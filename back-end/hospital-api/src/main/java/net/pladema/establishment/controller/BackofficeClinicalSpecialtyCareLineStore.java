package net.pladema.establishment.controller;

import net.pladema.establishment.repository.ClinicalSpecialtyCareLineRepository;
import net.pladema.establishment.repository.entity.ClinicalSpecialtyCareLine;
import net.pladema.sgx.backoffice.repository.BackofficeStore;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BackofficeClinicalSpecialtyCareLineStore implements BackofficeStore<ClinicalSpecialtyCareLine, Integer> {

    private final ClinicalSpecialtyCareLineRepository repository;

    public BackofficeClinicalSpecialtyCareLineStore(ClinicalSpecialtyCareLineRepository clinicalSpecialtyCareLineRepository) {
        this.repository = clinicalSpecialtyCareLineRepository;
    }

    @Override
    public Page<ClinicalSpecialtyCareLine> findAll(ClinicalSpecialtyCareLine entity, Pageable pageable) {
        List<ClinicalSpecialtyCareLine> result = this.repository.findByCareLineId(entity.getCareLineId());
        return new PageImpl<>(result, pageable, result.size());
    }

    @Override
    public List<ClinicalSpecialtyCareLine> findAll() {
        List<ClinicalSpecialtyCareLine> result = this.repository.findAll();
        return result;
    }

    @Override
    public List<ClinicalSpecialtyCareLine> findAllById(List<Integer> ids) {
        return this.repository.findAllById(ids);
    }

    @Override
    public Optional<ClinicalSpecialtyCareLine> findById(Integer id) {
        return this.repository.findById(id);
    }

    @Override
    public ClinicalSpecialtyCareLine save(ClinicalSpecialtyCareLine entity) {
        ClinicalSpecialtyCareLine clinicalSpecialtyCareLine = this.repository.findByCareLineIdAndClinicalSpecialtyId(entity.getCareLineId(), entity.getClinicalSpecialtyId());
        if (clinicalSpecialtyCareLine != null && clinicalSpecialtyCareLine.isDeleted()) {
            return update(clinicalSpecialtyCareLine);
        }
        return create(entity);
    }

    public ClinicalSpecialtyCareLine update(ClinicalSpecialtyCareLine entity) {
        entity.setDeleted(false);
        return this.repository.save(entity);
    }

    public ClinicalSpecialtyCareLine create(ClinicalSpecialtyCareLine entity) {
        return this.repository.save(entity);
    }

    @Override
    public void deleteById(Integer id) {
        ClinicalSpecialtyCareLine clinicalSpecialtyCareLine = this.repository.findById(id).get();
        this.repository.deleteById(clinicalSpecialtyCareLine.getId());
    }

    @Override
    public Example<ClinicalSpecialtyCareLine> buildExample(ClinicalSpecialtyCareLine entity) {
        return Example.of(entity);
    }
}
