package net.pladema.staff.application;

import lombok.AllArgsConstructor;

import net.pladema.staff.application.ports.HealthcareProfessionalStorage;
import net.pladema.staff.domain.ProfessionalCompleteBo;

import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class FetchProfessionalsByIds {

	private HealthcareProfessionalStorage healthcareProfessionalStorage;

	public List<ProfessionalCompleteBo> execute(List<Integer> professionalIds) {
		return healthcareProfessionalStorage.fetchProfessionalsByIds(professionalIds);
	}

}
