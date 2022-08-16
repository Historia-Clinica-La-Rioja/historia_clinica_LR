import { ChangeDetectionStrategy, Component, Input } from '@angular/core';
import { ProfessionalLicenseNumberDto, ProfessionalProfessionsDto } from '@api-rest/api-model';

const NATIONAL = 1;
const PROVINCE = 2;

@Component({
	selector: 'app-card-license',
	templateUrl: './card-license.component.html',
	styleUrls: ['./card-license.component.scss'],
	changeDetection: ChangeDetectionStrategy.OnPush
})
export class CardLicenseComponent  {

	NATIONAL = NATIONAL;
	PROVINCE = PROVINCE;
	@Input() professionsWithLicense: ProfessionalLicenseNumberDto[];
	@Input() ownProfessionsAndSpecialties: ProfessionalProfessionsDto[];

	convertToProfessionalLicenseNumberDto(elem: ProfessionalLicenseNumberDto): ProfessionalLicenseNumberDto {
		return {
			id: elem?.id || null,
			licenseNumber: elem.licenseNumber,
			professionalProfessionId: elem.professionalProfessionId,
			typeId: elem.typeId,
			healthcareProfessionalSpecialtyId: elem?.healthcareProfessionalSpecialtyId || null,
		}
	}

}
