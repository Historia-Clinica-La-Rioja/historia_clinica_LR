package net.pladema.reports.imageNetworkProductivity.application.port;

import net.pladema.reports.imageNetworkProductivity.domain.CellDataBo;
import net.pladema.reports.imageNetworkProductivity.domain.ImageNetworkProductivityFilterBo;

import java.util.List;

public interface ImageNetworkProductivityReportStorage {

	List<CellDataBo> fetchCellData(ImageNetworkProductivityFilterBo filter);

}
