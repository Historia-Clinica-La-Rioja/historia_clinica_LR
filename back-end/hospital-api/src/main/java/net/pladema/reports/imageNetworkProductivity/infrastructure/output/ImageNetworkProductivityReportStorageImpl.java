package net.pladema.reports.imageNetworkProductivity.infrastructure.output;

import lombok.AllArgsConstructor;
import net.pladema.reports.imageNetworkProductivity.application.port.ImageNetworkProductivityReportStorage;

import net.pladema.reports.imageNetworkProductivity.domain.CellDataBo;
import net.pladema.reports.imageNetworkProductivity.domain.ImageNetworkProductivityFilterBo;

import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class ImageNetworkProductivityReportStorageImpl implements ImageNetworkProductivityReportStorage {

	private ImageNetworkProductivityCellDataRepository imageNetworkProductivityCellDataRepository;

	@Override
	public List<CellDataBo> fetchCellData(ImageNetworkProductivityFilterBo filter) {
		return imageNetworkProductivityCellDataRepository.run(filter);
	}

}
