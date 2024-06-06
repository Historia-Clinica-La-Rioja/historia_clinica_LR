package net.pladema.address.controller.service.domain;

import lombok.Getter;
import net.pladema.address.repository.domain.AddressVo;

@Getter
public class AddressBo {

    private Integer id;

    private String street;

    private String number;

    private String floor;

    private String apartment;

    private String postcode;

    private CityBo city;

	private Short countryId;

	private String countryName;

	private Short provinceId;

	private Short departmentId;

	private String departmentName;

	private String bahraCode;

	public AddressBo (AddressVo addressVo){
        this.id = addressVo.getId();
        this.street = addressVo.getStreet();
        this.number = addressVo.getNumber();
        this.floor = addressVo.getFloor();
        this.apartment = addressVo.getApartment();
        this.city = new CityBo(addressVo.getCityId(), addressVo.getCityDescription());
        this.postcode = addressVo.getPostcode();
		this.countryId = addressVo.getCountryId();
		this.provinceId = addressVo.getProvinceId();
		this.departmentId = addressVo.getDepartmentId();
    }

    public AddressBo(Integer id, String street,
					 String number, String floor,
					 String apartment, String postcode,
					 Integer cityId, String cityDescription,
					 Short countryId, Short provinceId,
					 Short departmentId, String departmentName,
					 String countryName, String bahraCode) {
    	this.id = id;
    	this.street = street;
    	this.number = number;
    	this.floor = floor;
    	this.apartment = apartment;
    	this.postcode = postcode;
    	this.city = new CityBo(cityId, cityDescription);
		this.postcode = postcode;
		this.countryId = countryId;
		this.provinceId = provinceId;
		this.departmentId = departmentId;
		this.departmentName = departmentName;
		this.countryName = countryName;
		this.bahraCode = bahraCode;
	}
}
