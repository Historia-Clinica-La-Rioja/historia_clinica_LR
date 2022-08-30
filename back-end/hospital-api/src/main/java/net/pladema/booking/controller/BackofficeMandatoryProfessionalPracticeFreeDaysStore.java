package net.pladema.booking.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import net.pladema.booking.controller.dto.BackofficeMandatoryProfessionalPracticeFreeDaysDto;
import net.pladema.booking.repository.BackofficeClinicalSpecialtyMandatoryMedicalPracticeRepository;
import net.pladema.booking.repository.BackofficeMandatoryProfessionalPracticeFreeDaysRepository;
import net.pladema.booking.repository.entity.BackofficeClinicalSpecialtyMandatoryMedicalPractice;
import net.pladema.booking.repository.entity.BackofficeMandatoryProfessionalPracticeFreeDays;
import net.pladema.sgx.backoffice.repository.BackofficeStore;

@Service
public class BackofficeMandatoryProfessionalPracticeFreeDaysStore
        implements BackofficeStore<BackofficeMandatoryProfessionalPracticeFreeDaysDto, Integer> {

    private final BackofficeMandatoryProfessionalPracticeFreeDaysRepository repository;
    private final BackofficeClinicalSpecialtyMandatoryMedicalPracticeRepository clinicalSpecialtyMandatoryMedicalPracticeRepository;

    public BackofficeMandatoryProfessionalPracticeFreeDaysStore(
            BackofficeMandatoryProfessionalPracticeFreeDaysRepository repository,
            BackofficeClinicalSpecialtyMandatoryMedicalPracticeRepository clinicalSpecialtyMandatoryMedicalPracticeRepository) {
        this.repository = repository;
        this.clinicalSpecialtyMandatoryMedicalPracticeRepository = clinicalSpecialtyMandatoryMedicalPracticeRepository;
    }

    @Override
    public Page<BackofficeMandatoryProfessionalPracticeFreeDaysDto> findAll(
            BackofficeMandatoryProfessionalPracticeFreeDaysDto example, Pageable pageable) {
        List<BackofficeMandatoryProfessionalPracticeFreeDaysDto> elements = getBackofficeMandatoryProfessionalPracticeFreeDaysDtos();
        return new PageImpl<>(elements, pageable, elements.size());
    }

    private List<BackofficeMandatoryProfessionalPracticeFreeDaysDto> getBackofficeMandatoryProfessionalPracticeFreeDaysDtos() {
        return repository
                .findAll().stream()
                .collect(Collectors.groupingBy(BackofficeMandatoryProfessionalPracticeFreeDays::getHealthcareProfessionalId,
                        Collectors.groupingBy(BackofficeMandatoryProfessionalPracticeFreeDays::getClinicalSpecialtyMandatoryMedicalPracticeId)))
                .values().stream()
                .map(Map::entrySet)
                .map(el -> el.stream().map(this::getDto).collect(Collectors.toList()))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    private BackofficeMandatoryProfessionalPracticeFreeDaysDto getDto(
            Map.Entry<Integer, List<BackofficeMandatoryProfessionalPracticeFreeDays>> entry) {
        var value = entry.getValue();

        var csmmp = clinicalSpecialtyMandatoryMedicalPracticeRepository
                .findById(value.get(0).getClinicalSpecialtyMandatoryMedicalPracticeId());

        var listDays = value.stream()
                .map(BackofficeMandatoryProfessionalPracticeFreeDays::getDay)
                .sorted()
                .collect(Collectors.toList());

        return csmmp.map(backofficeClinicalSpecialtyMandatoryMedicalPractice -> new BackofficeMandatoryProfessionalPracticeFreeDaysDto(
                value.get(0).getId(),
                value.get(0).getHealthcareProfessionalId(),
                backofficeClinicalSpecialtyMandatoryMedicalPractice.getClinicalSpecialtyId(),
                backofficeClinicalSpecialtyMandatoryMedicalPractice.getMandatoryMedicalPracticeId(),
                listDays
        )).orElseGet(BackofficeMandatoryProfessionalPracticeFreeDaysDto::new);
    }

    @Override
    public List<BackofficeMandatoryProfessionalPracticeFreeDaysDto> findAll() {
        return getBackofficeMandatoryProfessionalPracticeFreeDaysDtos();
    }

    @Override
    public List<BackofficeMandatoryProfessionalPracticeFreeDaysDto> findAllById(List<Integer> ids) {
        List<BackofficeMandatoryProfessionalPracticeFreeDaysDto> result = new ArrayList<>();
        for (Integer id : ids) {
            var res = this.findById(id);
            res.ifPresent(result::add);
        }
        return result;
    }

    @Override
    public Optional<BackofficeMandatoryProfessionalPracticeFreeDaysDto> findById(Integer id) {
        return this.findAll().stream().filter(el -> id.equals(el.getId())).findFirst();
    }

    @Override
    public BackofficeMandatoryProfessionalPracticeFreeDaysDto save(BackofficeMandatoryProfessionalPracticeFreeDaysDto entity) {
        entity.setDays(entity.getDays().stream().distinct().collect(Collectors.toList()));

        var csmmp = new BackofficeClinicalSpecialtyMandatoryMedicalPractice();
        csmmp.setClinicalSpecialtyId(entity.getClinicalSpecialtyId());
        csmmp.setMandatoryMedicalPracticeId(entity.getMandatoryMedicalPracticeId());

        var foundCsmmp = clinicalSpecialtyMandatoryMedicalPracticeRepository
                .findOne(Example.of(csmmp));

        if(foundCsmmp.isEmpty()) {
            csmmp.setPracticeRecommendations("No hay recomendaciones especiales");
            csmmp.setId(clinicalSpecialtyMandatoryMedicalPracticeRepository.save(csmmp).getId());
        } else {
            csmmp.setId(foundCsmmp.get().getId());
        }

        entity.getDays().forEach(day -> entity.setId(saveEntity(day, csmmp.getId(), entity)));
        return entity;
    }

    private Integer saveEntity(Short day, Integer csmmpId,
                            BackofficeMandatoryProfessionalPracticeFreeDaysDto entity) {
        var toSave = new BackofficeMandatoryProfessionalPracticeFreeDays(
                csmmpId, entity.getHealthcareProfessionalId(), day
        );

        var existingEntity = repository.findOne(Example.of(toSave));
        if(existingEntity.isEmpty())
            return repository.save(toSave).getId();
        return existingEntity.get().getId();
    }

    @Override
    public void deleteById(Integer id) {
        var dto= this.findById(id);
        if(dto.isPresent()) {
            var foundDto = dto.get();
            foundDto.getDays().forEach(day -> delete(foundDto, day));
        }
    }

    private void delete(BackofficeMandatoryProfessionalPracticeFreeDaysDto foundDto, Short day) {
        var csmmp = new BackofficeClinicalSpecialtyMandatoryMedicalPractice();
        csmmp.setClinicalSpecialtyId(foundDto.getClinicalSpecialtyId());
        csmmp.setMandatoryMedicalPracticeId(foundDto.getMandatoryMedicalPracticeId());

        var foundCsmmp = clinicalSpecialtyMandatoryMedicalPracticeRepository
                .findOne(Example.of(csmmp));

        if(foundCsmmp.isPresent()) {
            var found = repository.
                    findOne(Example.of(
                            new BackofficeMandatoryProfessionalPracticeFreeDays(
                                foundCsmmp.get().getId(),
                                foundDto.getHealthcareProfessionalId(),
                                day)));
            
            found.ifPresent(backofficeMandatoryProfessionalPracticeFreeDays ->
                    repository.deleteById(backofficeMandatoryProfessionalPracticeFreeDays.getId()));
        }
    }

    @Override
    public Example<BackofficeMandatoryProfessionalPracticeFreeDaysDto> buildExample(BackofficeMandatoryProfessionalPracticeFreeDaysDto entity) {
        return null;
    }
}
