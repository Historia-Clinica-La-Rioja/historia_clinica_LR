package net.pladema.booking.controller;

import net.pladema.sgx.backoffice.repository.BackofficeStore;
import net.pladema.booking.controller.dto.HealthcareProfessionalHealthInsuranceDto;
import net.pladema.booking.repository.HealthcareProfessionalHealthInsuranceRepository;
import net.pladema.booking.repository.entity.HealthcareProfessionalHealthInsurance;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BackofficeHealthcareProfessionalHealthInsuranceStore implements
        BackofficeStore<HealthcareProfessionalHealthInsuranceDto, Integer> {

    private final HealthcareProfessionalHealthInsuranceRepository repository;

    public BackofficeHealthcareProfessionalHealthInsuranceStore(
            HealthcareProfessionalHealthInsuranceRepository healthcareProfessionalHealthInsuranceRepository) {
        this.repository = healthcareProfessionalHealthInsuranceRepository;
    }

    @Override
    public Page<HealthcareProfessionalHealthInsuranceDto> findAll(HealthcareProfessionalHealthInsuranceDto example, Pageable pageable) {
        List<HealthcareProfessionalHealthInsuranceDto> content = repository.findAll().stream()
                .map(this::map)
                .collect(Collectors.toList());
        return new PageImpl<>(content, pageable, content.size());
    }

    private HealthcareProfessionalHealthInsuranceDto map(HealthcareProfessionalHealthInsurance healthcareProfessionalHealthInsurance) {
        return new HealthcareProfessionalHealthInsuranceDto(
                healthcareProfessionalHealthInsurance.getId(),
                healthcareProfessionalHealthInsurance.getMedicalCoverageId(),
                healthcareProfessionalHealthInsurance.getHealthcareProfessionalId()
        );
    }

    @Override
    public List<HealthcareProfessionalHealthInsuranceDto> findAll() {
        return repository.findAll().stream().map(this::map).collect(Collectors.toList());
    }


    @Override
    public List<HealthcareProfessionalHealthInsuranceDto> findAllById(List<Integer> ids) {
        return repository.findAllById(ids).stream().map(this::map).collect(Collectors.toList());
    }

    @Override
    public Optional<HealthcareProfessionalHealthInsuranceDto> findById(Integer id) {
        return repository.findById(id).map(this::map);
    }

    @Override
    public HealthcareProfessionalHealthInsuranceDto save(HealthcareProfessionalHealthInsuranceDto entity) {
        HealthcareProfessionalHealthInsurance healthcareProfessionalHealthInsurance = new HealthcareProfessionalHealthInsurance(
                entity.getId(),
                entity.getHealthcareProfessionalId(),
                entity.getMedicalCoverageId()
        );
        return map(repository.save(healthcareProfessionalHealthInsurance));
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public Example<HealthcareProfessionalHealthInsuranceDto> buildExample(HealthcareProfessionalHealthInsuranceDto entity) {
        return null;
    }


}
