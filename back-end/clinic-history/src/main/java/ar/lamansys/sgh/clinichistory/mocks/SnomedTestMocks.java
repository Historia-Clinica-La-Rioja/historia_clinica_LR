package ar.lamansys.sgh.clinichistory.mocks;


import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.ips.Snomed;

public class SnomedTestMocks {

    public static Snomed createSnomed(String id) {
        Snomed result = new Snomed();
        result.setSctid(id);
        result.setParentId(id);
        result.setPt(id);
        result.setParentFsn(id);
        return result;
    }

}
