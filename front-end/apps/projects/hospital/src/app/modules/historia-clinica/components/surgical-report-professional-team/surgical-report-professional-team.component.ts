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

	constructor(private dialog: MatDialog, private requestMasterDataService: RequestMasterDataService) { }

	ngOnInit(): void {
		this.surgeon = this.surgicalReport.healthcareProfessionals.find(p => p.profession.type === EProfessionType.SURGEON)
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
			}
		})
		dialogRef.afterClosed().subscribe((professional: AddMemberMedicalTeam) => {
			if (professional) {
				this.surgicalReport.healthcareProfessionals.push(professional.professionalData);
				this.healthcareProfessionals.push(professional);
			}
		})
	}

	setHealthcareProfessionals() {
		this.healthcareProfessionals = this.surgicalReport.healthcareProfessionals
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
		this.surgicalReport.healthcareProfessionals.splice(index, 1);
	}

	selectSurgeon(professional: HCEHealthcareProfessionalDto): void {
		const index = this.surgicalReport.healthcareProfessionals.findIndex(p => p.profession.type === EProfessionType.SURGEON);

		if (professional && index == -1)
			this.surgicalReport.healthcareProfessionals.push(this.mapToDocumentHealthcareProfessionalDto(professional, EProfessionType.SURGEON));

		if (professional && index != -1)
			this.surgicalReport.healthcareProfessionals.splice(index, 1, this.mapToDocumentHealthcareProfessionalDto(professional, EProfessionType.SURGEON));

		if (!professional && index != -1)
			this.surgicalReport.healthcareProfessionals.splice(index, 1);
	}

	isEmpty(): boolean {
		return !this.surgicalReport.healthcareProfessionals.length;
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
