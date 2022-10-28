package ar.lamansys.sgh.publicapi.application.port.out;

import java.util.List;

import ar.lamansys.sgh.publicapi.domain.BedRelocationInfoBo;
import ar.lamansys.sgh.publicapi.domain.DocumentInfoBo;
import ar.lamansys.sgh.publicapi.domain.ProcedureInformationBo;
import ar.lamansys.sgh.publicapi.domain.SupplyInformationBo;

public interface ActivityInfoStorage {

	void processActivity(String refsetCode, Long activityId);

	List<ProcedureInformationBo> getProceduresByActivity(String refsetCode, Long activityId);

	List<SupplyInformationBo> getSuppliesByActivity(String refsetCode, Long activityId);

	List<BedRelocationInfoBo> getBedRelocationsByActivity(String refsetCode, Long activityId);

	List<DocumentInfoBo> getDocumentsByActivity(String refsetCode, Long activityId);
}