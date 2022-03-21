package ar.lamansys.sgh.clinichistory.domain.ips;

import lombok.Getter;
import ar.lamansys.sgx.shared.exceptions.NotFoundException;

@Getter
public enum EUnitsOfTimeBo {

	SECOND("s"),
    MINUTE("m"),
    HOUR("h"),
    DAY("d"),
    WEEK("w"),
    MONTH("m"),
    ANNUAL("a"),
	EVENT("e");

    private String value;

    EUnitsOfTimeBo(String value) {
        this.value = value;
    }

    public String getValue(){
        return value;
    }

    public static EUnitsOfTimeBo map(String value) {
        for(EUnitsOfTimeBo e : values()) {
            if(e.value.equals(value)) return e;
        }
        throw new NotFoundException("value-not-exists", String.format("La unidad de tiempo %s no existe", value));
    }
}
