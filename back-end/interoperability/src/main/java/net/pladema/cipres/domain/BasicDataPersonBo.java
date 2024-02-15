package net.pladema.cipres.domain;

import lombok.Getter;
import lombok.Builder;
import lombok.ToString;

@ToString
@Builder
@Getter
public class BasicDataPersonBo {

	private Integer id;

	private Short identificationTypeId;

	private String identificationNumber;

	private Short genderId;

}