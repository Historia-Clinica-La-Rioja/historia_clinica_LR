package ar.lamansys.sgh.publicapi.application.port.out;

import ar.lamansys.sgh.publicapi.infrastructure.output.AttentionReads;

public interface AttentionReadStorage {
	void saveAttention(AttentionReads ar);
}
