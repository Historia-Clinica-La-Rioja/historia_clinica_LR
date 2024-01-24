package ar.lamansys.sgh.clinichistory.domain.ips;

import ar.lamansys.sgh.clinichistory.domain.ips.enums.EAnesthesiaZone;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EPreviousAnesthesiaState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import static java.util.Objects.nonNull;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AnestheticHistoryBo {

    private Long id;
    private EPreviousAnesthesiaState state;
    private EAnesthesiaZone zone;

    public AnestheticHistoryBo(Long id, Short stateId, Short zoneId) {
        this.id = id;
        this.state = EPreviousAnesthesiaState.map(stateId);
        this.zone = nonNull(zoneId) ? EAnesthesiaZone.map(zoneId) : null;
    }

    public Short getStateId() {
        return state.getId();
    }

    public Short getZoneId() {
        return nonNull(zone) ? zone.getId() : null;
    }
}
