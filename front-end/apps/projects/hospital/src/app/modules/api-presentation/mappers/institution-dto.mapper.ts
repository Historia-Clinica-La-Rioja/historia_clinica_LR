import { InstitutionAddressDto, InstitutionDto } from '@api-rest/api-model';
import { LocationInfo } from '@presentation/components/location-badge/location-badge.component';

export const mapToLocation = (institutionDto: InstitutionDto): LocationInfo => (
	{
		name: institutionDto.name,
		address: institutionDto.institutionAddressDto.city.description
	}
);

export const mapToAddress = (institution: InstitutionAddressDto): string =>
	[institution?.street, institution?.number, institution?.floor, institution?.apartment].filter(item => !!item).join(' ');
