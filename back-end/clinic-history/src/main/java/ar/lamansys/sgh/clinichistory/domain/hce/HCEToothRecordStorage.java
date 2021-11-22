package ar.lamansys.sgh.clinichistory.domain.hce;

import java.util.List;

public interface HCEToothRecordStorage {

    List<HCEToothRecordBo> getToothRecords(Integer patientId, String toothSctid);

}
