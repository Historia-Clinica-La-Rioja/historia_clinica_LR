package net.pladema.clinichistory.mocks;

import net.pladema.clinichistory.documents.repository.ips.masterdata.entity.Snomed;

public class SnomedTestMocks {

    public static Snomed createSnomed(String id) {
        Snomed result = new Snomed();
        result.setId(id);
        result.setParentId(id);
        result.setPt(id);
        result.setParentFsn(id);
        return result;
    }

}
