package net.pladema.staff.repository.entity.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import net.pladema.staff.service.domain.ELicenseNumberTypeBo;

@Converter
public class LicenseNumberTypeConverter implements AttributeConverter<ELicenseNumberTypeBo, Short> {
 
    @Override
    public Short convertToDatabaseColumn(ELicenseNumberTypeBo attribute) {
        return attribute.getId();
    }
 
    @Override
    public ELicenseNumberTypeBo convertToEntityAttribute(Short dbData) {
        return ELicenseNumberTypeBo.map(dbData);
    }
 
}