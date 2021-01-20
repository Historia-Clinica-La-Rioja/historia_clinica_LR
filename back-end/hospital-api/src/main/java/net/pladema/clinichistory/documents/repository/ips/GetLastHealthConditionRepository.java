package net.pladema.clinichistory.documents.repository.ips;

import java.util.List;

public interface GetLastHealthConditionRepository {
    List<Object[]> run(Integer patientId, List<Integer> hcIds);
}
