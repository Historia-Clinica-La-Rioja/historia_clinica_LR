package net.pladema.establishment.controller;

import net.pladema.establishment.controller.dto.MedicalCoverageTypeDto;
import net.pladema.patient.controller.dto.EMedicalCoverageType;
import net.pladema.sgx.backoffice.repository.BackofficeStore;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class BackofficeMedicalCoverageTypeStore implements BackofficeStore<MedicalCoverageTypeDto, Short> {

    @Override
    public List<MedicalCoverageTypeDto> findAll() {
        return getMedicalCoverageType();
    }

    private List<MedicalCoverageTypeDto> getMedicalCoverageType() {
        return Stream.of(EMedicalCoverageType.values())
                .map(tr -> new MedicalCoverageTypeDto(tr.getId(), tr.getValue()))
                .collect(Collectors.toList());
    }

    @Override
    public Page<MedicalCoverageTypeDto> findAll(MedicalCoverageTypeDto example, Pageable pageable) {
        List<MedicalCoverageTypeDto> list = getMedicalCoverageType();
        return new PageImpl<>(list, pageable, list.size());
    }

    @Override
    public List<MedicalCoverageTypeDto> findAllById(List<Short> ids) {
        if (ids.isEmpty())
            return getMedicalCoverageType();
        return getMedicalCoverageType().stream()
                .filter(dto -> ids.contains(dto.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<MedicalCoverageTypeDto> findById(Short id) {
        return getMedicalCoverageType().stream()
                .filter(dto -> id.equals(dto.getId())).findFirst();
    }

    @Override
    public MedicalCoverageTypeDto save(MedicalCoverageTypeDto entity) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void deleteById(Short id) {
        // TODO Auto-generated method stub
    }

    @Override
    public Example<MedicalCoverageTypeDto> buildExample(MedicalCoverageTypeDto entity) {
        return Example.of(entity);
    }
}
