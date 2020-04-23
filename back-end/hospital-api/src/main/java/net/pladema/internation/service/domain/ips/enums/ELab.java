package net.pladema.internation.service.domain.ips.enums;


public enum ELab {

	BLOOD_TYPE("365636006", "34532-2");

    private String loincCode;

    private String sctidCode;

    ELab(String sctidCode, String loincCode) {
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
