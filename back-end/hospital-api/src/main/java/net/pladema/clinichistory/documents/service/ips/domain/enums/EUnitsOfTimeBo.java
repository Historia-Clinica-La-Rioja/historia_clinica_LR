package net.pladema.clinichistory.documents.service.ips.domain.enums;

import lombok.Getter;

@Getter
public enum EUnitsOfTimeBo {

	SECOND("s"),
    MINUTE("m"),
    HOUR("h"),
    DAY("d"),
    WEEK("w"),
    MONTH("m"),
    ANNUAL("a");

    private String value;

    EUnitsOfTimeBo(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }
}
