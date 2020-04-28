package net.pladema.internation.service.domain.ips;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ClinicalObservation implements Serializable {

    private Integer id;

    private String value;

    private boolean deleted = false;

    public boolean mustSave() {
        return isDeleted() || isNew();
    }

    private boolean isNew() {
        return id == null;
    }

}
