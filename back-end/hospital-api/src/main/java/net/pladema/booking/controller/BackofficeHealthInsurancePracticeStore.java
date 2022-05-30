package net.pladema.booking.controller;

import net.pladema.booking.controller.dto.BackofficeHealthInsurancePracticeDto;
import net.pladema.booking.repository.BackofficeClinicalSpecialtyMandatoryMedicalPracticeRepository;
import net.pladema.booking.repository.BackofficeHealthInsurancePracticeRepository;
import net.pladema.booking.repository.entity.BackofficeClinicalSpecialtyMandatoryMedicalPractice;
import net.pladema.booking.repository.entity.BackofficeHealthInsurancePractice;
import net.pladema.sgx.backoffice.repository.BackofficeStore;
import net.pladema.sgx.exceptions.BackofficeValidationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BackofficeHealthInsurancePracticeStore implements BackofficeStore<BackofficeHealthInsurancePracticeDto, Integer> {

    private final BackofficeHealthInsurancePracticeRepository repository;
    private final BackofficeClinicalSpecialtyMandatoryMedicalPracticeRepository clinicalSpecialtyMandatoryMedicalPracticeRepository;

    public BackofficeHealthInsurancePracticeStore(BackofficeHealthInsurancePracticeRepository repository,
                                                  BackofficeClinicalSpecialtyMandatoryMedicalPracticeRepository
                                                          clinicalSpecialtyMandatoryMedicalPracticeRepository) {
        this.repository = repository;
        this.clinicalSpecialtyMandatoryMedicalPracticeRepository = clinicalSpecialtyMandatoryMedicalPracticeRepository;
    }

    @Override
    public Page<BackofficeHealthInsurancePracticeDto> findAll(BackofficeHealthInsurancePracticeDto example, Pageable pageable) {
        List<BackofficeHealthInsurancePracticeDto> content = repository.findAll().stream()
                .map(this::map)
                .collect(Collectors.toList());
        return new PageImpl<>(content, pageable, content.size());
    }

    private BackofficeHealthInsurancePracticeDto map(BackofficeHealthInsurancePractice backofficeHealthInsurancePractice) {
        var csmmp =
        clinicalSpecialtyMandatoryMedicalPracticeRepository.findById(backofficeHealthInsurancePractice.getClinicalSpecialtyMandatoryMedicalPracticeId());
        return csmmp.map(backofficeClinicalSpecialtyMandatoryMedicalPractice -> new BackofficeHealthInsurancePracticeDto(
                backofficeHealthInsurancePractice.getId(),
                backofficeClinicalSpecialtyMandatoryMedicalPractice.getClinicalSpecialtyId(),
                backofficeClinicalSpecialtyMandatoryMedicalPractice.getMandatoryMedicalPracticeId(),
                backofficeHealthInsurancePractice.getMedicalCoverageId(),
                backofficeHealthInsurancePractice.getCoverageInformation()
        )).orElseGet(BackofficeHealthInsurancePracticeDto::new);

    }

    @Override
    public List<BackofficeHealthInsurancePracticeDto> findAll() {
        return repository.findAll().stream().map(this::map).collect(Collectors.toList());
    }

    @Override
    public List<BackofficeHealthInsurancePracticeDto> findAllById(List<Integer> ids) {
        return repository.findAllById(ids).stream().map(this::map).collect(Collectors.toList());
    }

    @Override
    public Optional<BackofficeHealthInsurancePracticeDto> findById(Integer id) {
        return repository.findById(id).map(this::map);
    }

    @Override
    public BackofficeHealthInsurancePracticeDto save(BackofficeHealthInsurancePracticeDto entity) {
        var csmmp = new BackofficeClinicalSpecialtyMandatoryMedicalPractice();
        csmmp.setClinicalSpecialtyId(entity.getClinicalSpecialtyId());
        csmmp.setMandatoryMedicalPracticeId(entity.getMandatoryMedicalPracticeId());
        var example = Example.of(csmmp);
        var foundCsmmp = clinicalSpecialtyMandatoryMedicalPracticeRepository.findOne(example);

        if(foundCsmmp.isEmpty()) {
            csmmp.setPracticeRecommendations("No hay recomendaciones especiales");
            csmmp.setId(clinicalSpecialtyMandatoryMedicalPracticeRepository.save(csmmp).getId());
        } else {
            csmmp.setId(foundCsmmp.get().getId());
        }
        var toSave = new BackofficeHealthInsurancePractice();

        toSave.setClinicalSpecialtyMandatoryMedicalPracticeId(csmmp.getId());
        toSave.setMedicalCoverageId(entity.getMedicalCoverageId());

        if(repository.exists(Example.of(toSave))) {
            throw new BackofficeValidationException("booking-healthinsurance-practice.exists");
        }

        toSave.setCoverageInformation(entity.getCoverageInformation());
        return map(repository.save(toSave));
    }

    @Override
    public void deleteById(Integer id) {
        repository.deleteById(id);
    }

    @Override
    public Example<BackofficeHealthInsurancePracticeDto> buildExample(BackofficeHealthInsurancePracticeDto entity) {
        return null;
    }
}
