import { ChangeDetectionStrategy, Component, EventEmitter, Input, Output } from '@angular/core';
import { ProfessionalLicenseNumberDto, ProfessionalProfessionsDto } from '@api-rest/api-model';
import { ProfessionalLicenseService } from '@api-rest/services/professional-license.service';
import { SnackBarService } from '@presentation/services/snack-bar.service';

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
	@Input() healthcareProfessionalId: number;
	@Output() itemEvent = new EventEmitter<ProfessionalLicenseNumberDto>();

	constructor(
		private readonly professionalLicenseService: ProfessionalLicenseService,
		private readonly snackBarService: SnackBarService,
	) {

	}

	convertToProfessionalLicenseNumberDto(elem: ProfessionalLicenseNumberDto): ProfessionalLicenseNumberDto {
		return {
			id: elem?.id || null,
			licenseNumber: elem.licenseNumber,
			professionalProfessionId: elem.professionalProfessionId,
			typeId: elem.typeId,
			healthcareProfessionalSpecialtyId: elem?.healthcareProfessionalSpecialtyId || null,
		}
	}

	delete(item: ProfessionalLicenseNumberDto) {
		this.professionalLicenseService.removeProfessionalLicenseNumber(this.healthcareProfessionalId, item).subscribe(data => {
			this.itemEvent.emit(item);
			this.snackBarService.showSuccess('pacientes.edit_professions.messages.SUCCESS');
		});
	}

}
