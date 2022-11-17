package net.pladema.medicalconsultation.diary.repository.entity;

import ar.lamansys.sgx.shared.auditable.entity.SGXAuditableEntity;
import ar.lamansys.sgx.shared.auditable.listener.SGXAuditListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

import java.io.Serializable;

@Entity
@Table(name = "diary_care_line")
@EntityListeners(SGXAuditListener.class)
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class DiaryCareLine extends SGXAuditableEntity<DiaryCareLinePk> implements Serializable {

	@EmbeddedId
	private DiaryCareLinePk pk;

	@Override
	public DiaryCareLinePk getId() {
		return this.pk;
	}

	public DiaryCareLine(Integer diaryId, Integer careLineId){
		this.pk = new DiaryCareLinePk(diaryId, careLineId);
	}
}
