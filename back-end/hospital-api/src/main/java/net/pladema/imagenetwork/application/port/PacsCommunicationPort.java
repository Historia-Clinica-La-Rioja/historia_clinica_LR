package net.pladema.imagenetwork.application.port;

import net.pladema.imagenetwork.domain.PacsBo;

public interface PacsCommunicationPort {
    PacsBo doHealthcheckProof(PacsBo pacsBo);
}
