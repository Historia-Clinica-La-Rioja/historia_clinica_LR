package net.pladema.address.controller.mocks;


import net.pladema.address.controller.dto.AddressDto;
import net.pladema.address.controller.dto.CityDto;
import net.pladema.address.controller.dto.ProvinceDto;
import net.pladema.person.controller.dto.HealthInsuranceDto;
import net.pladema.person.controller.dto.IdentificationTypeDto;

public class MocksAddress {

    private MocksAddress(){
        super();
    }

    public static AddressDto mockAddressDto(int id) {
        AddressDto result = new AddressDto();
        result.setId(id);
        result.setStreet("Calle");
        result.setNumber("123");
        result.setFloor("1");
        result.setApartment("A");
        result.setPostcode("1234");
        result.setQuarter("Barrio");
        result.setCity(mockCityDto(id));
        result.setProvince(mockProvinceDto(id));
        return result;
    }

    public static CityDto mockCityDto(int id) {
        CityDto result = new CityDto();
        result.setId(id);
        result.setDescription("Ciudad");
        return result;
    }

    public static ProvinceDto mockProvinceDto(int id) {
        ProvinceDto result = new ProvinceDto();
        result.setId((short)id);
        result.setDescription("Provincia");
        return result;
    }

    private static HealthInsuranceDto mockHealthInsuranceDto(Integer id){
        HealthInsuranceDto result = new HealthInsuranceDto();
        result.setId(Short.valueOf(id + ""));
        result.setAcronym("OSDE");
        result.setRnos("0123456");
        return result;
    }

    private static IdentificationTypeDto mockIdentificationTypeDto(Integer id){
        IdentificationTypeDto result = new IdentificationTypeDto();
        result.setId(Short.valueOf(id + ""));
        result.setDescription("DNI");
        return result;
    }
}
