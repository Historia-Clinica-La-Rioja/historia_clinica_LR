import { Component, Input, OnInit } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DocumentHealthcareProfessionalDto, EProfessionType, GenericMasterDataDto, HCEHealthcareProfessionalDto, ProfessionalDto, SurgicalReportDto } from '@api-rest/api-model';
import { RequestMasterDataService } from '@api-rest/services/request-masterdata.service';
import { AddMemberMedicalTeam, AddMemberMedicalTeamComponent } from '@historia-clinica/dialogs/add-member-medical-team/add-member-medical-team.component';

@Component({
	selector: 'app-surgical-report-professional-team',
	templateUrl: './surgical-report-professional-team.component.html',
	styleUrls: ['./surgical-report-professional-team.component.scss']
})

export class SurgicalReportProfessionalTeamComponent implements OnInit {

	@Input() professionals: ProfessionalDto[];
	@Input() surgicalReport: SurgicalReportDto;

	healthcareProfessionals: AddMemberMedicalTeam[] = [];
	surgeon: DocumentHealthcareProfessionalDto;
	readonly OTHER = EProfessionType.OTHER;
	professions: GenericMasterDataDto<EProfessionType>[];
	showErrorProfessionalRepeated = false;

	constructor(private dialog: MatDialog, private requestMasterDataService: RequestMasterDataService) { }

	ngOnInit(): void {
		this.surgeon = this.surgicalReport.surgicalTeam.find(p => p.profession.type === EProfessionType.SURGEON)
		this.requestMasterDataService.getSurgicalReportProfessionTypes().subscribe(professions => {
			professions.shift();
			this.professions = professions;
			this.setHealthcareProfessionals();
		})
	}

	addProfessional(): void {
		const dialogRef = this.dialog.open(AddMemberMedicalTeamComponent, {
			data: {
				professionals: this.professionals,
				professions: this.professions,
				idProfessionalSelected: this.surgeon.healthcareProfessional.id,
			}
		})
		dialogRef.afterClosed().subscribe((professional: AddMemberMedicalTeam) => {
			if (professional) {
				this.surgicalReport.surgicalTeam.push(professional.professionalData);
				this.healthcareProfessionals.push(professional);
			}
		})
	}

	setHealthcareProfessionals() {
		this.healthcareProfessionals = this.surgicalReport.surgicalTeam
			.filter(hp => hp.profession.type !== EProfessionType.SURGEON)
			.map(hp => {
				let professional = {
					professionalData: hp,
					descriptionType: null,
				};
				const profession = this.professions.find(profession => hp.profession.type === profession.id);
				if (profession && profession.id !== this.OTHER) {
					professional.descriptionType = profession.description;
				}
				return professional;
			});
	}

	deleteProfessional(index: number): void {
		this.healthcareProfessionals.splice(index, 1);
		this.surgicalReport.surgicalTeam.splice(index, 1);
	}

	selectSurgeon(professional: HCEHealthcareProfessionalDto | null): void {
		this.showErrorProfessionalRepeated = false;

		this.surgicalReport.surgicalTeam = this.surgicalReport.surgicalTeam.filter(p => p.profession.type !== EProfessionType.SURGEON);

		if (!professional) {
			this.surgeon = null;
			return;
		}
		this.surgeon = this.mapToDocumentHealthcareProfessionalDto(professional, EProfessionType.SURGEON);

		const existingProfessional = this.surgicalReport.surgicalTeam.find(p => p.healthcareProfessional.id === professional.id);

		existingProfessional ? this.showErrorProfessionalRepeated = true : this.surgicalReport.surgicalTeam.push(this.surgeon);
	}

	isEmpty(): boolean {
		return !this.surgicalReport.surgicalTeam.length;
	}

	private mapToDocumentHealthcareProfessionalDto(professional: HCEHealthcareProfessionalDto, type: EProfessionType, description?: string): DocumentHealthcareProfessionalDto {
		return {
			healthcareProfessional: professional,
			profession: {
				type: type,
				otherTypeDescription: description
			}
		}
	}
}
