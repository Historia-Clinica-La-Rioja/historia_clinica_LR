package net.pladema.violencereport.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder
@EqualsAndHashCode
public class ViolenceReportAddressBo {

	private Short provinceId;

	private Short municipalityId;

	private String municipalityName;

	private Integer cityId;

	private String cityName;

	private String homeAddress;

}
