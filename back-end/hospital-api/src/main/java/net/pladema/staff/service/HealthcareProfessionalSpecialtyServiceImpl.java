package net.pladema.staff.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import net.pladema.staff.repository.ProfessionalProfessionRepository;
import net.pladema.staff.service.domain.ProfessionalProfessionsBo;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import net.pladema.staff.repository.HealthcareProfessionalSpecialtyRepository;
import net.pladema.staff.repository.domain.ProfessionalClinicalSpecialtyVo;
import net.pladema.staff.repository.entity.ClinicalSpecialty;
import net.pladema.staff.service.domain.HealthcareProfessionalSpecialtyBo;
import net.pladema.staff.service.domain.ProfessionalsByClinicalSpecialtyBo;

@Service
@RequiredArgsConstructor
public class HealthcareProfessionalSpecialtyServiceImpl implements HealthcareProfessionalSpecialtyService {

    private final HealthcareProfessionalSpecialtyRepository healthcareProfessionalSpecialtyRepository;

	private final ProfessionalProfessionRepository professionalProfessionRepository;

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
    public List<ProfessionalProfessionsBo> getProfessionsByProfessional(Integer professionalId) {
        List<ProfessionalProfessionsBo> result = professionalProfessionRepository.findByHealthcareProfessionalId(professionalId)
				.stream().map(ProfessionalProfessionsBo::new).collect(Collectors.toList());
		result.forEach(pp->
			pp.setSpecialties(healthcareProfessionalSpecialtyRepository.getAllByProfessional(pp.getId())
					.stream().map(HealthcareProfessionalSpecialtyBo::new).collect(Collectors.toList())));
        return result;
    }

	@Override
	public List<ProfessionalsByClinicalSpecialtyBo> getProfessionalsByActiveDiaryAndClinicalSpecialtyBo(List<Integer> professionalsIds, Integer institutionId) {
		List<ProfessionalClinicalSpecialtyVo> professionalsSpecialties = this.healthcareProfessionalSpecialtyRepository
				.getAllByActiveDiaryAndProfessionals(professionalsIds, institutionId)
				.stream()
				.filter(ProfessionalClinicalSpecialtyVo::isSpecialty)
				.collect(Collectors.toList());

		List<Integer> healthcareProfessionalIdsWithActiveDiaries = healthcareProfessionalSpecialtyRepository.getProfessionalsByActiveDiary(institutionId);

		List<ProfessionalsByClinicalSpecialtyBo> professionalsBySpecialty = new ArrayList<>();

		professionalsSpecialties.forEach(professionalSpecialty -> {
			if (healthcareProfessionalIdsWithActiveDiaries.contains(professionalSpecialty.getProfessionalId()))
				addToProfessionalsBySpecialty(professionalSpecialty, professionalsBySpecialty);
		});

		return professionalsBySpecialty;
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
