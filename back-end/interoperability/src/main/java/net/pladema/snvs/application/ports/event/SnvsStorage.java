package net.pladema.snvs.application.ports.event;

import net.pladema.snvs.application.ports.event.exceptions.SnvsStorageException;
import net.pladema.snvs.domain.event.SnvsEventInfoBo;
import net.pladema.snvs.domain.event.SnvsEventManualClassificationsBo;
import net.pladema.snvs.domain.event.exceptions.SnvsEventInfoBoException;
import net.pladema.snvs.domain.problem.SnvsProblemBo;

import java.util.List;
import java.util.Optional;

public interface SnvsStorage {

    Optional<SnvsEventInfoBo> fetchSnvsEventInfo(SnvsProblemBo problemBo, Integer manualClassificationId, Integer groupEventId, Integer eventId) throws SnvsEventInfoBoException, SnvsStorageException;

    List<SnvsEventManualClassificationsBo> fetchManualClassification(SnvsProblemBo problemBo);
}
