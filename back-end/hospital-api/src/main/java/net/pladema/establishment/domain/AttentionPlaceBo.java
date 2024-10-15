package net.pladema.establishment.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Optional;

@AllArgsConstructor
@Getter
public class AttentionPlaceBo {
	Integer id;
	Optional<Integer> statusId;
	Boolean isFree;

	public boolean isBlocked() {
		return this.getStatusId().isPresent();
	}
}
