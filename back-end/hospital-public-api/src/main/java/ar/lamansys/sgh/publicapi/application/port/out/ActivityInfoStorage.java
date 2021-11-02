package ar.lamansys.sgh.publicapi.application.port.out;

import ar.lamansys.sgh.publicapi.domain.BedRelocationInfoBo;
import ar.lamansys.sgh.publicapi.domain.SnomedBo;

import java.util.List;

public interface ActivityInfoStorage {

    void processActivity(String refsetCode, String provinceCode, Long activityId);

    List<SnomedBo> getProceduresByActivity(String refsetCode, String provinceCode, Long activityId);

    List<SnomedBo> getSuppliesByActivity(String refsetCode, String provinceCode, Long activityId);

    List<BedRelocationInfoBo> getBedRelocationsByActivity(String refsetCode, String provinceCode, Long activityId);
}
