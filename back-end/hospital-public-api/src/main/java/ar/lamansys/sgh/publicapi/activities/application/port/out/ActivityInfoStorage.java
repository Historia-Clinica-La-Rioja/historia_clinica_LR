package ar.lamansys.sgh.publicapi.activities.application.port.out;

import java.util.List;

import ar.lamansys.sgh.publicapi.activities.domain.BedRelocationInfoBo;
import ar.lamansys.sgh.publicapi.activities.domain.DocumentInfoBo;
import ar.lamansys.sgh.publicapi.activities.domain.ProcedureInformationBo;
import ar.lamansys.sgh.publicapi.activities.domain.SupplyInformationBo;

public interface ActivityInfoStorage {

	List<ProcedureInformationBo> getProceduresByActivity(String refsetCode, Long activityId);

	List<SupplyInformationBo> getSuppliesByActivity(String refsetCode, Long activityId);

	List<BedRelocationInfoBo> getBedRelocationsByActivity(String refsetCode, Long activityId);

	List<DocumentInfoBo> getDocumentsByActivity(String refsetCode, Long activityId);
}