package net.pladema.clinichistory.requests.servicerequests.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.annotation.Nullable;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class SnomedItemBo implements Serializable {
	private static final long serialVersionUID = -198432836028268437L;

	@Nullable
	private Integer id;

	private String sctid;

	private String pt;

	private String parentId;

	private String parentFsn;

	public SnomedItemBo(String sctid, String pt) {
		this.sctid = sctid;
		this.pt = pt;
	}

	public SnomedItemBo(String sctid, String pt, String parentId, String parentFsn) {
		this.sctid = sctid;
		this.pt = pt;
		this.parentId = parentId;
		this.parentFsn = parentFsn;
	}

	public SnomedItemBo(Integer id, String sctid, String pt) {
		this.id = id;
		this.sctid = sctid;
		this.pt = pt;
	}
}
