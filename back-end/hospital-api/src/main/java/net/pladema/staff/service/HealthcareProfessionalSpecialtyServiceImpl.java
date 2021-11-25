package net.pladema.staff.service;

import lombok.RequiredArgsConstructor;
import net.pladema.staff.repository.DeletedHealthcareProfessionalSpecialtyRepositoryImpl;
import net.pladema.staff.repository.HealthcareProfessionalSpecialtyRepository;
import net.pladema.staff.repository.domain.HealthcareProfessionalSpecialtyVo;
import net.pladema.staff.repository.domain.ProfessionalClinicalSpecialtyVo;
import net.pladema.staff.repository.entity.ClinicalSpecialty;
import net.pladema.staff.repository.entity.HealthcareProfessionalSpecialty;
import net.pladema.staff.service.domain.HealthcareProfessionalSpecialtyBo;
import net.pladema.staff.service.domain.ProfessionalsByClinicalSpecialtyBo;
import net.pladema.staff.service.exceptions.HealthcareProfessionalSpecialtyEnumException;
import net.pladema.staff.service.exceptions.HealthcareProfessionalSpecialtyException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HealthcareProfessionalSpecialtyServiceImpl implements HealthcareProfessionalSpecialtyService {

    private final HealthcareProfessionalSpecialtyRepository healthcareProfessionalSpecialtyRepository;

    private final DeletedHealthcareProfessionalSpecialtyRepositoryImpl findHealthcareProfessionalSpecialtyRepository;

    @Override
    public List<ProfessionalsByClinicalSpecialtyBo> getProfessionalsByClinicalSpecialtyBo(List<Integer> professionalsIds) {

        List<ProfessionalClinicalSpecialtyVo> professionalsSpecialties = this.healthcareProfessionalSpecialtyRepository
                .getAllByProfessionals(professionalsIds)
                .stream()
                .filter(ProfessionalClinicalSpecialtyVo::isSpecialty)
                .collect(Collectors.toList());

        List<ProfessionalsByClinicalSpecialtyBo> professionalsBySpecialty = new ArrayList<>();

        professionalsSpecialties.forEach(pS -> this.addToProfessionalsBySpecialty(pS, professionalsBySpecialty));

        return professionalsBySpecialty;
    }

    @Override
    public List<HealthcareProfessionalSpecialtyBo> getProfessionsByProfessional(Integer professionalId) {
        List<HealthcareProfessionalSpecialtyBo> result = new ArrayList<>();
        healthcareProfessionalSpecialtyRepository.getAllByProfessional(professionalId)
                .forEach(healthcareProfessionalSpecialtyVo ->
                        result.add(mapToHealthcareProfessionalSpecialtyBo(healthcareProfessionalSpecialtyVo))
                );
        return result;
    }

    @Override
    public Integer saveProfessionalSpeciality(HealthcareProfessionalSpecialtyBo healthcareProfessionalSpecialtyBo){
        return (healthcareProfessionalSpecialtyBo.getId()==null)? createProfessionalSpecialty(healthcareProfessionalSpecialtyBo)
                : changeProfessionalSpecialty(healthcareProfessionalSpecialtyBo);
    }

    private Integer createProfessionalSpecialty(HealthcareProfessionalSpecialtyBo healthcareProfessionalSpecialtyBo){
        if(healthcareProfessionalSpecialtyRepository
                .existsValues(
                        healthcareProfessionalSpecialtyBo.getHealthcareProfessionalId(),
                        healthcareProfessionalSpecialtyBo.getClinicalSpecialtyId(),
                        healthcareProfessionalSpecialtyBo.getProfessionalSpecialtyId()))
            throw new HealthcareProfessionalSpecialtyException(HealthcareProfessionalSpecialtyEnumException.DUPLICATE_PROFESSIONAL_SPECIALTY, "La profesión y especialidad ya se encuentra asignada");
        HealthcareProfessionalSpecialty saved = healthcareProfessionalSpecialtyRepository.save(new HealthcareProfessionalSpecialty(
                healthcareProfessionalSpecialtyBo.getHealthcareProfessionalId(),
                healthcareProfessionalSpecialtyBo.getProfessionalSpecialtyId(),
                healthcareProfessionalSpecialtyBo.getClinicalSpecialtyId()
        ));
        return saved.getId();
    }

    private Integer changeProfessionalSpecialty(HealthcareProfessionalSpecialtyBo professionalSpecialtyBo) {
        Optional<HealthcareProfessionalSpecialtyBo> duplicate = Optional.ofNullable(findHealthcareProfessionalSpecialtyRepository
                .getHealthcareProfessionalSpecialty(professionalSpecialtyBo.getHealthcareProfessionalId(),
                        professionalSpecialtyBo.getClinicalSpecialtyId(),
                        professionalSpecialtyBo.getProfessionalSpecialtyId()));
        return duplicate.map(hpsBo -> {
            if (hpsBo.getDeleted()) {
                healthcareProfessionalSpecialtyRepository.setDeletedFalse(hpsBo.getId());
                if(!Objects.equals(hpsBo.getId(), professionalSpecialtyBo.getId()))
                    healthcareProfessionalSpecialtyRepository.deleteById(professionalSpecialtyBo.getId());
                return hpsBo.getId();
            } else
                throw new HealthcareProfessionalSpecialtyException(HealthcareProfessionalSpecialtyEnumException.DUPLICATE_PROFESSIONAL_SPECIALTY, "La profesión y especialidad ya se encuentra asignada");
        }).orElseGet(() -> {
            HealthcareProfessionalSpecialty entity = healthcareProfessionalSpecialtyRepository.save(new HealthcareProfessionalSpecialty(
                    professionalSpecialtyBo.getHealthcareProfessionalId(),
                    professionalSpecialtyBo.getProfessionalSpecialtyId(),
                    professionalSpecialtyBo.getClinicalSpecialtyId()));
            healthcareProfessionalSpecialtyRepository.deleteById(professionalSpecialtyBo.getId());
            return entity.getId();
        });
    }

    @Override
    public void delete(Integer id) {
        healthcareProfessionalSpecialtyRepository.findById(id)
                .ifPresent(e ->healthcareProfessionalSpecialtyRepository.deleteById(id));
    }

    private HealthcareProfessionalSpecialtyBo mapToHealthcareProfessionalSpecialtyBo(HealthcareProfessionalSpecialtyVo hpsVo) {
        return new HealthcareProfessionalSpecialtyBo(
                hpsVo.getId(),
                hpsVo.getHealthcareProfessionalId(),
                hpsVo.getProfessionalSpecialtyId(),
                hpsVo.getClinicalSpecialtyId()
        );
    }

    private void addToProfessionalsBySpecialty(ProfessionalClinicalSpecialtyVo pSVo,
                                               List<ProfessionalsByClinicalSpecialtyBo> professionalsSpecialties) {

        ProfessionalsByClinicalSpecialtyBo professionalsBySpecialty = professionalsSpecialties.stream()
                .filter(professionalSpecialty -> professionalSpecialty.getClinicalSpecialty().getId()
                        .equals((pSVo.getClinicalSpecialty().getId())))
                .findAny()
                .orElse(null);

        if (professionalsBySpecialty != null) {
            professionalsBySpecialty.addProfessionalId(pSVo.getProfessionalId());
        } else {
            ProfessionalsByClinicalSpecialtyBo professionalsByClinicalSpecialtyBo =
                    createProfessionalsBySpecialtyBo(pSVo.getProfessionalId(), pSVo.getClinicalSpecialty());
            professionalsSpecialties.add(professionalsByClinicalSpecialtyBo);
        }

    }

    private ProfessionalsByClinicalSpecialtyBo createProfessionalsBySpecialtyBo(Integer professionalId, ClinicalSpecialty specialty) {
        List<Integer> professionalsIds = new ArrayList<>();
        professionalsIds.add(professionalId);

        return new ProfessionalsByClinicalSpecialtyBo(specialty, professionalsIds);
    }

}
