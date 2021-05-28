package ar.lamansys.sgh.clinichistory.domain.ips;


public enum EObservationLab {

	BLOOD_TYPE("365636006", "34532-2");

    private String loincCode;

    private String sctidCode;

    EObservationLab(String sctidCode, String loincCode) {
        this.sctidCode = sctidCode;
        this.loincCode = loincCode;
    }

    public String getLoincCode() {
        return loincCode;
    }

    public String getSctidCode() {
        return sctidCode;
    }
}
