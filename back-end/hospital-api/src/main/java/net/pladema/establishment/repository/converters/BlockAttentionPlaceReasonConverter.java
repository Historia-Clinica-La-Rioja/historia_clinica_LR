package net.pladema.establishment.repository.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import net.pladema.establishment.domain.EBlockAttentionPlaceReason;

@Converter
public class BlockAttentionPlaceReasonConverter implements AttributeConverter<EBlockAttentionPlaceReason, Short> {

	@Override
	public Short convertToDatabaseColumn(EBlockAttentionPlaceReason attribute) {
		return attribute.getId();
	}

	@Override
	public EBlockAttentionPlaceReason convertToEntityAttribute(Short dbData) {
		return EBlockAttentionPlaceReason.map(dbData);
	}

}