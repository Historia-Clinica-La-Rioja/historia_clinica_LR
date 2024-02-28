package net.pladema.reports.imageNetworkProductivity.infrastructure.output;

import lombok.AllArgsConstructor;
import net.pladema.reports.imageNetworkProductivity.application.port.ImageNetworkProductivityReportInstitutionStorage;
import net.pladema.reports.imageNetworkProductivity.domain.InstitutionBo;

import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class ImageNetworkProductivityReportInstitutionStorageImpl implements ImageNetworkProductivityReportInstitutionStorage {

	private ImageNetworkInstitutionRepository imageNetworkInstitutionRepository;

	@Override
	public InstitutionBo fetchInstitutionData(Integer institutionId) {
		return imageNetworkInstitutionRepository.getImageNetworkProductivityReportInstitution(institutionId);
	}

}
