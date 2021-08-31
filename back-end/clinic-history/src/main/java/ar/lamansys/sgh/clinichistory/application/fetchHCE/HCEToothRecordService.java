package ar.lamansys.sgh.clinichistory.application.fetchHCE;

import ar.lamansys.sgh.clinichistory.domain.hce.HCEToothRecordBo;
import java.util.List;

public interface HCEToothRecordService {

    List<HCEToothRecordBo> getToothRecords(Integer patientId, String toothSctid);

}
