package net.pladema.staff.service;

import lombok.RequiredArgsConstructor;
import net.pladema.staff.repository.ProfessionalSpecialtyRepository;
import net.pladema.staff.repository.entity.ProfessionalSpecialty;
import net.pladema.staff.service.domain.ProfessionalSpecialtyBo;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProfessionalSpecialtyServiceImpl implements ProfessionalSpecialtyService {

    private final ProfessionalSpecialtyRepository professionalSpecialtyRepository;

    @Override
    public List<ProfessionalSpecialtyBo> getAll() {
        List<ProfessionalSpecialtyBo> result = new ArrayList<>();
        professionalSpecialtyRepository.findAll()
                .forEach(professionalSpecialty -> result.add(mapToBo(professionalSpecialty)));
        return result;
    }

    private ProfessionalSpecialtyBo mapToBo(ProfessionalSpecialty entity) {
        return new ProfessionalSpecialtyBo(entity.getId(), entity.getDescription());
    }
}
