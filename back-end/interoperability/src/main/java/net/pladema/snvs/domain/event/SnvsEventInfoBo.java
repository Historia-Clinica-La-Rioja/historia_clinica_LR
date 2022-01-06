package net.pladema.snvs.domain.event;

import lombok.Getter;
import net.pladema.snvs.domain.event.exceptions.SnvsEventInfoBoEnumException;
import net.pladema.snvs.domain.event.exceptions.SnvsEventInfoBoException;

@Getter
public class SnvsEventInfoBo {

    private final Integer eventId;

    private final Integer groupEventId;

    private final Integer environment;

    private final Integer manualClassificationId;

    public SnvsEventInfoBo(Integer eventId, Integer groupEventId, Integer manualClassificationId, Integer environment) throws SnvsEventInfoBoException {
        if (eventId == null)
            throw new SnvsEventInfoBoException(SnvsEventInfoBoEnumException.NULL_EVENT_ID,"El id del evento es obligatoria");
        this.eventId = eventId;
        if (groupEventId == null)
            throw new SnvsEventInfoBoException(SnvsEventInfoBoEnumException.NULL_GROUP_EVENT_ID,"El id del grupo evento es obligatoria");
        this.groupEventId = groupEventId;
        if (manualClassificationId == null)
            throw new SnvsEventInfoBoException(SnvsEventInfoBoEnumException.NULL_MANUAL_CLASSIFICATION_ID,"El id de la clasificación manual es obligatoria");
        this.manualClassificationId = manualClassificationId;
        this.environment = environment;
    }
}