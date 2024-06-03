package ar.lamansys.sgh.clinichistory.domain.ips;

import ar.lamansys.sgh.clinichistory.domain.ips.enums.EAnesthesiaZone;
import ar.lamansys.sgh.clinichistory.domain.ips.enums.EPreviousAnesthesiaState;
import ar.lamansys.sgh.clinichistory.domain.ips.visitor.IpsVisitor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import static java.util.Objects.nonNull;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class AnestheticHistoryBo implements IpsBo {

    private Long id;
    private EPreviousAnesthesiaState state;
    private EAnesthesiaZone zone;

    public AnestheticHistoryBo(Long id, Short stateId, Short zoneId) {
        this.id = id;
        this.state = nonNull(stateId) ? EPreviousAnesthesiaState.map(stateId) : null;
        this.zone = nonNull(zoneId) ? EAnesthesiaZone.map(zoneId) : null;
    }

    public Short getStateId() {
        return nonNull(state) ? state.getId() : null;
    }

    public Short getZoneId() {
        return nonNull(zone) ? zone.getId() : null;
    }

    @Override
    public void accept(IpsVisitor visitor) {
        visitor.visitAnestheticHistory(this);
    }
}
