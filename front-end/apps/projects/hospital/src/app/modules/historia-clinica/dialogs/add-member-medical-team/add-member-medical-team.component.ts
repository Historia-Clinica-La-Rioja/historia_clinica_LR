import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { DocumentHealthcareProfessionalDto, EProfessionType, GenericMasterDataDto, HCEHealthcareProfessionalDto, ProfessionalDto } from '@api-rest/api-model';
@Component({
	selector: 'app-add-member-medical-team',
	templateUrl: './add-member-medical-team.component.html',
	styleUrls: ['./add-member-medical-team.component.scss']
})
export class AddMemberMedicalTeamComponent {
	professional: AddMemberMedicalTeam = {
		professionalData: {
			healthcareProfessional: {
				id: null,
				licenseNumber: null,
				person: null,
			},
			profession: {
				type: null,
				otherTypeDescription: null,
			}
		},
		descriptionType: null,
	};
	showErrorProfessionalRepeated = false;
	isConfirmDisabled = true;
	readonly otherProfession = EProfessionType.OTHER;

	constructor(
		@Inject(MAT_DIALOG_DATA) public data: {
			professionals: ProfessionalDto[],
			professions: GenericMasterDataDto<EProfessionType>[],
			healthcareProfessionals: DocumentHealthcareProfessionalDto[],
		},
		public dialogRef: MatDialogRef<AddMemberMedicalTeamComponent>)
	{
		this.checkConfirmButtonStatus();
	}

	selectProfessional(professional: HCEHealthcareProfessionalDto) {
		const exists = this.data.healthcareProfessionals.some(
			item => item.healthcareProfessional.id === professional?.id
		);

		if (!exists) {
			this.professional.professionalData.healthcareProfessional = professional;
			this.showErrorProfessionalRepeated = false;
		} else {
			this.showErrorProfessionalRepeated = true;
		}
		this.checkConfirmButtonStatus();
	}

	emitProfessionalSelected() {
		this.professional.descriptionType = this.data.professions.find(
			profession => this.professional.professionalData.profession.type === profession.id).description;
		this.dialogRef.close(this.professional ? this.professional : null);
	}

	private checkConfirmButtonStatus() {
		this.isConfirmDisabled = !this.professional.professionalData.profession.type ||
			!this.professional.professionalData?.healthcareProfessional?.id ||
			this.showErrorProfessionalRepeated;
	}
}

export interface AddMemberMedicalTeam {
	professionalData: DocumentHealthcareProfessionalDto,
	descriptionType: string
}
