package net.pladema.establishment.repository.entity;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import net.pladema.establishment.domain.EBlockAttentionPlaceReason;
import net.pladema.establishment.repository.converters.BlockAttentionPlaceReasonConverter;

import javax.persistence.Column;
import javax.persistence.Converter;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.Date;

@Entity
@Table(name = "attention_place_status")
@Getter
@Setter
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class AttentionPlaceStatus {
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@EqualsAndHashCode.Include
	private Integer id;

	@Column(name = "is_blocked", nullable = false)
	private Boolean isBlocked;

	@Column(name = "user_id", nullable = false)
	private Integer userId;

	@Column(name = "created_on", nullable = false)
	private ZonedDateTime createdOn;

	@Column(name = "reason_id", nullable = false)
	private Short reasonId;

	@Column(name = "reason", nullable = false)
	private String reason;

	static public AttentionPlaceStatus blockedNow(Integer userId, EBlockAttentionPlaceReason reasonEnum,
		String reason
	) {
		AttentionPlaceStatus ret = new AttentionPlaceStatus();
		ret.setIsBlocked(true);
		ret.setUserId(userId);
		ret.setReasonId(reasonEnum.getId());
		ret.setReason(reason);
		ret.setCreatedOn(ZonedDateTime.now());
		return ret;
	}

}
