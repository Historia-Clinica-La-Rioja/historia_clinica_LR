package ar.lamansys.sgh.clinichistory.domain;

import ar.lamansys.sgx.shared.validation.CustomContainer;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@ToString
@Getter
@Setter
public class ReferableItemBo<T> extends CustomContainer<T> {

	private Boolean isReferred;

	public ReferableItemBo() {
		super();
		isReferred = null;
	}

	public ReferableItemBo(List<T> content, Boolean isReferred) {
		super(content);
		this.isReferred = isReferred;
	}

}
