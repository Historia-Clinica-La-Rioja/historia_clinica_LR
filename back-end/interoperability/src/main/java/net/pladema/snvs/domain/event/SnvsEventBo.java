package net.pladema.snvs.domain.event;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import net.pladema.snvs.domain.event.exceptions.SnvsEventInfoBoEnumException;
import net.pladema.snvs.domain.event.exceptions.SnvsEventInfoBoException;

@Getter
@EqualsAndHashCode
@ToString
public class SnvsEventBo {

    private final String description;

    @ToString.Include
    @EqualsAndHashCode.Include
    private final Integer eventId;

    @ToString.Include
    @EqualsAndHashCode.Include
    private final Integer groupEventId;

    @ToString.Include
    @EqualsAndHashCode.Include
    private final Integer environment;

    public SnvsEventBo(String description, Integer eventId, Integer groupEventId, Integer environment) throws SnvsEventInfoBoException {
        if (description == null)
            throw new SnvsEventInfoBoException(SnvsEventInfoBoEnumException.NULL_DESCRIPTION,"La descripción del evento es obligatoria");
        this.description = description;
        if (eventId == null)
            throw new SnvsEventInfoBoException(SnvsEventInfoBoEnumException.NULL_EVENT_ID,"El id del evento es obligatorio");
        this.eventId = eventId;
        if (groupEventId == null)
            throw new SnvsEventInfoBoException(SnvsEventInfoBoEnumException.NULL_GROUP_EVENT_ID,"El id del grupo evento es obligatorio");
        this.groupEventId = groupEventId;
        if (environment == null)
            throw new SnvsEventInfoBoException(SnvsEventInfoBoEnumException.NULL_ENVIRONMENT_ID,"El id del ambiente es obligatorio");
        this.environment = environment;
    }
}
