package ar.lamansys.sgh.clinichistory.domain.ips;

import ar.lamansys.sgh.clinichistory.domain.ips.enums.EInternmentPlace;
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
public class PostAnesthesiaStatusBo {

    private Long id;
    private Boolean intentionalSensitivity;
    private Boolean cornealReflex;
    private Boolean obeyOrders;
    private Boolean talk;
    private Boolean respiratoryDepression;
    private Boolean circulatoryDepression;
    private Boolean vomiting;
    private Boolean curated;
    private Boolean trachealCannula;
    private Boolean pharyngealCannula;
    private Boolean internment;
    private EInternmentPlace internmentPlace;
    private String note;

    public PostAnesthesiaStatusBo(Long id, Boolean intentionalSensitivity, Boolean cornealReflex, Boolean obeyOrders,
                                  Boolean talk, Boolean respiratoryDepression, Boolean circulatoryDepression,
                                  Boolean vomiting, Boolean curated, Boolean trachealCannula, Boolean pharyngealCannula,
                                  Boolean internment, Short internmentPlaceId, String note) {
        this.id = id;
        this.intentionalSensitivity = intentionalSensitivity;
        this.cornealReflex = cornealReflex;
        this.obeyOrders = obeyOrders;
        this.talk = talk;
        this.respiratoryDepression = respiratoryDepression;
        this.circulatoryDepression = circulatoryDepression;
        this.vomiting = vomiting;
        this.curated = curated;
        this.trachealCannula = trachealCannula;
        this.pharyngealCannula = pharyngealCannula;
        this.internment = internment;
        this.internmentPlace = internmentPlaceId != null ? EInternmentPlace.map(internmentPlaceId) : null;
        this.note = note;
    }

    public Short getIntermentPlaceId() {
        return internmentPlace != null ? internmentPlace.getId() : null;
    }
}
