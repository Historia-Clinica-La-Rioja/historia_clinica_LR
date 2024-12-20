package net.pladema.medicalconsultation.diary.domain.enums;

import ar.lamansys.sgx.shared.exceptions.NotFoundException;
import lombok.Getter;

@Getter
public enum EDiaryBookingRestrictionType {

    UNRESTRICTED(-1,"UNRESTRICTED"),
    RESTRICTED_BY_DAYS_AMOUNT(1,"RESTRICTED_BY_DAYS_AMOUNT"),
    RESTRICTED_BY_CURRENT_MONTH(2,"RESTRICTED_BY_CURRENT_MONTH");

    private Short id;
    private String value;

    EDiaryBookingRestrictionType(Number id, String value) {
        this.id = id.shortValue();
        this.value = value;
    }

    public void setId(Short id) {
        this.id = id;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static EDiaryBookingRestrictionType map(Short id) {
        for (EDiaryBookingRestrictionType e : values()) {
            if (e.id.equals(id)) return e;
        }
        throw new NotFoundException("booking-restriction-type-not-exists", String.format("El tipo de restricción para agendas %s no existe", id));
    }
}
