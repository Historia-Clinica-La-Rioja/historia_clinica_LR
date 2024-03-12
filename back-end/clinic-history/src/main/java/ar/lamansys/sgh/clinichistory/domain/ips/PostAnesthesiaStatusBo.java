package ar.lamansys.sgh.clinichistory.domain.ips;

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
    private Short internmentPlace;
    private String note;
}
