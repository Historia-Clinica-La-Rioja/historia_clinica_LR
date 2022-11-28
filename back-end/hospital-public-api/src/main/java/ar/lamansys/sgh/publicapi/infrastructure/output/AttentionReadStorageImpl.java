package ar.lamansys.sgh.publicapi.infrastructure.output;

import ar.lamansys.sgh.publicapi.application.port.out.AttentionReadStorage;

import org.springframework.stereotype.Service;

@Service

public class AttentionReadStorageImpl implements AttentionReadStorage {

	private final AttentionReadsRepository attentionReadsRepository;

	public AttentionReadStorageImpl(AttentionReadsRepository attentionReadsRepository) {
		this.attentionReadsRepository = attentionReadsRepository;
	}

	@Override
	public void saveAttention(AttentionReads ar) {
		var attentions = attentionReadsRepository.findByAttentionId(ar.getAttentionReadsPK().getAttentionId());
		if(attentions == null || attentions.size() == 0) {
			attentionReadsRepository.save(ar);
		}
	}
}
