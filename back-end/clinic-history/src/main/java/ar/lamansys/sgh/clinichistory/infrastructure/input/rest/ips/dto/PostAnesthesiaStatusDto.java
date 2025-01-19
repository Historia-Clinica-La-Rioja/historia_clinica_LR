package ar.lamansys.sgh.clinichistory.infrastructure.input.rest.ips.dto;

import ar.lamansys.sgh.clinichistory.domain.ips.enums.EInternmentPlace;
import javax.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class PostAnesthesiaStatusDto {

    @Nullable
    private Boolean intentionalSensitivity;

    @Nullable
    private Boolean cornealReflex;

    @Nullable
    private Boolean obeyOrders;

    @Nullable
    private Boolean talk;

    @Nullable
    private Boolean respiratoryDepression;

    @Nullable
    private Boolean circulatoryDepression;

    @Nullable
    private Boolean vomiting;

    @Nullable
    private Boolean curated;

    @Nullable
    private Boolean trachealCannula;

    @Nullable
    private Boolean pharyngealCannula;

    @Nullable
    private Boolean internment;

    @Nullable
    private EInternmentPlace internmentPlace;

    @Nullable
    private String note;

    public boolean hasValues() {
        return intentionalSensitivity != null
                || cornealReflex != null
                || obeyOrders != null
                || talk != null
                || respiratoryDepression != null
                || circulatoryDepression != null
                || vomiting != null
                || curated != null
                || trachealCannula != null
                || pharyngealCannula != null
                || internment != null
                || internmentPlace != null
                || (note != null && !note.isEmpty());
    }
}
