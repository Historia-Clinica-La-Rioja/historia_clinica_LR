import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
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
	@Output() validSurgeon = new EventEmitter<boolean>();

	healthcareProfessionals: AddMemberMedicalTeam[] = [];
	surgeon: DocumentHealthcareProfessionalDto;
	readonly OTHER = EProfessionType.OTHER;
	professions: GenericMasterDataDto<EProfessionType>[];
	showErrorProfessionalRepeated = false;
	isSelectedSurgeon: boolean | null = null

	constructor(
		private dialog: MatDialog,
		private requestMasterDataService: RequestMasterDataService
	) { }

	ngOnInit(): void {
		this.surgeon = this.surgicalReport.surgicalTeam.find(p => p.profession.type === EProfessionType.SURGEON);
		this.emitValidSurgeon();
		if (this.surgeon) {
			this.surgicalReport.surgicalTeam = this.surgicalReport.surgicalTeam.filter(p => p.profession.type !== EProfessionType.SURGEON);
			this.surgicalReport.surgicalTeam.unshift(this.surgeon);
		}
		this.requestMasterDataService.getSurgicalReportProfessionTypes().subscribe(professions => {
			professions.shift();
			this.professions = professions;
			this.setHealthcareProfessionals();
		});
	}

	addProfessional(): void {
		const dialogRef = this.dialog.open(AddMemberMedicalTeamComponent, {
			data: {
				professionals: this.professionals,
				professions: this.professions,
				idProfessionalSelected: this.surgeon.healthcareProfessional.id,
			}
		});
		dialogRef.afterClosed().subscribe((professional: AddMemberMedicalTeam) => {
			if (professional) {
				this.surgicalReport.surgicalTeam.push(professional.professionalData);
				this.healthcareProfessionals.push(professional);
				this.emitValidSurgeon();
			}
		});
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
		const actualIndex = this.surgeon ? index + 1 : index;
		this.healthcareProfessionals.splice(index, 1);
		this.surgicalReport.surgicalTeam.splice(actualIndex, 1);
		this.emitValidSurgeon();
	}

	selectSurgeon(professional: HCEHealthcareProfessionalDto | null): void {
		this.showErrorProfessionalRepeated = false;
		this.surgicalReport.surgicalTeam = this.surgicalReport.surgicalTeam.filter(p => p.profession.type !== EProfessionType.SURGEON);

		if (!professional) {
			this.surgeon = null;
			this.emitValidSurgeon();
			return;
		}

		this.surgeon = this.mapToDocumentHealthcareProfessionalDto(professional, EProfessionType.SURGEON);
		const existingProfessional = this.surgicalReport.surgicalTeam.find(p => p.healthcareProfessional.id === professional.id);

		if (existingProfessional) {
			this.showErrorProfessionalRepeated = true;
		}
		this.surgicalReport.surgicalTeam.unshift(this.surgeon);
		this.emitValidSurgeon();
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
		};
	}

	protected emitValidSurgeon() {
		const surgicalTeam = this.surgicalReport.surgicalTeam.length ? this.surgicalReport.surgicalTeam : null;
		let hasSurgeon: boolean | null = null;

		if (surgicalTeam) {
			hasSurgeon = surgicalTeam.some(p => p.profession.type === EProfessionType.SURGEON);
		}

		this.validSurgeon.emit(hasSurgeon && !this.showErrorProfessionalRepeated);
		this.isSelectedSurgeon = hasSurgeon;
	}
}
