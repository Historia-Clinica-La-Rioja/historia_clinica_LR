package net.pladema.establishment.sanitaryresponsibilityarea.institutionresponsibilityarea.infrastructure.output;

import lombok.RequiredArgsConstructor;
import net.pladema.establishment.sanitaryresponsibilityarea.institutionresponsibilityarea.application.port.output.SaveInstitutionResponsibilityAreaPort;
import net.pladema.establishment.sanitaryresponsibilityarea.institutionresponsibilityarea.domain.SaveInstitutionResponsibilityAreaBo;

import net.pladema.establishment.sanitaryresponsibilityarea.institutionresponsibilityarea.infrastructure.output.repository.InstitutionResponsibilityAreaRepository;

import net.pladema.establishment.sanitaryresponsibilityarea.institutionresponsibilityarea.infrastructure.output.repository.entity.InstitutionResponsibilityArea;

import net.pladema.establishment.sanitaryresponsibilityarea.institutionresponsibilityarea.infrastructure.output.repository.entity.InstitutionResponsibilityAreaPK;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SaveInstitutionResponsibilityAreaPortImpl implements SaveInstitutionResponsibilityAreaPort {

	private final InstitutionResponsibilityAreaRepository institutionResponsibilityAreaRepository;

	@Override
	public void run(SaveInstitutionResponsibilityAreaBo saveInstitutionResponsibilityAreaBo) {
		institutionResponsibilityAreaRepository.saveAll(parseToInstitutionResponsibilityAreaList(saveInstitutionResponsibilityAreaBo));
	}

	private List<InstitutionResponsibilityArea> parseToInstitutionResponsibilityAreaList(SaveInstitutionResponsibilityAreaBo saveInstitutionResponsibilityAreaBo) {
		List<InstitutionResponsibilityArea> result = new ArrayList<>();
		for (short order = 0; order < saveInstitutionResponsibilityAreaBo.getResponsibilityAreaPolygon().size(); order++)
			result.add(parseToInstitutionResponsibilityArea(saveInstitutionResponsibilityAreaBo, order));
		return result;
	}

	private InstitutionResponsibilityArea parseToInstitutionResponsibilityArea(SaveInstitutionResponsibilityAreaBo saveInstitutionResponsibilityAreaBo, short order) {
		InstitutionResponsibilityArea result = new InstitutionResponsibilityArea();
		result.setPk(new InstitutionResponsibilityAreaPK(saveInstitutionResponsibilityAreaBo.getInstitutionId(), order));
		result.setLatitude(saveInstitutionResponsibilityAreaBo.getResponsibilityAreaPolygon().get(order).getLatitude());
		result.setLongitude(saveInstitutionResponsibilityAreaBo.getResponsibilityAreaPolygon().get(order).getLongitude());
		return result;
	}

}
