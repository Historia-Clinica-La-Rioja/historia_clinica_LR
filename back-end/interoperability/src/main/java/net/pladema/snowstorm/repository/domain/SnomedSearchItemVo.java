package net.pladema.snowstorm.repository.domain;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Normalized;

import java.text.Normalizer;
import java.util.Locale;
import java.util.Objects;

@AllArgsConstructor
@ToString
@Getter
public class SnomedSearchItemVo {

    private Integer snomedId;

    private String sctid;

    private String pt;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof SnomedSearchItemVo)) return false;
		SnomedSearchItemVo that = (SnomedSearchItemVo) o;
		return sctid.equals(that.getSctid())
				&& StringUtils.stripAccents(pt).equalsIgnoreCase(StringUtils.stripAccents(that.getPt()));
	}

	@Override
	public int hashCode() {
		return Objects.hash(getSctid());
	}

}
