import { Component, Input, OnInit } from '@angular/core';
import { DocumentHealthcareProfessionalDto, EProfessionType, HCEHealthcareProfessionalDto, HealthcareProfessionalDto, SurgicalReportDto } from '@api-rest/api-model';

const PROFESSIONAL_TYPE = {
	[EProfessionType.PATHOLOGIST]: 'pathologist',
	[EProfessionType.TRANSFUSIONIST]: 'transfusionist'
};
@Component({
	selector: 'app-professional-and-description',
	templateUrl: './professional-and-description.component.html',
	styleUrls: ['./professional-and-description.component.scss']
})
export class ProfessionalAndDescriptionComponent implements OnInit {

	@Input() title: string;
	@Input() professionalTitle: string;
	@Input() professionals: HealthcareProfessionalDto[];
	@Input() icon: string;
	@Input() type: EProfessionType;
	@Input() surgicalReport: SurgicalReportDto;

	description: string;
	professional: DocumentHealthcareProfessionalDto;

	private readonly professionalType = PROFESSIONAL_TYPE;

	constructor() { }

	ngOnInit(): void {
		this.professional = this.getProfessionalByType(this.type);
		this.description = this.professional?.comments
	}

	changeProfessional(professional: HCEHealthcareProfessionalDto): void {
		if (professional) {
			this.professional = this.mapToDocumentHealthcareProfessionalDto(professional);
			this.setProfessionalByType(this.professional, this.type);
		} else {
			this.deleteProfessional(this.type);
		}
	}

	changeDescription(description: string): void {
		this.description = description;
		if (this.professional) {
			this.professional.comments = description;
			this.setProfessionalByType(this.professional, this.type);
		}
	}

	private mapToDocumentHealthcareProfessionalDto(professional: HCEHealthcareProfessionalDto): DocumentHealthcareProfessionalDto {
		return {
			healthcareProfessional: professional,
			profession: {
				type: this.type,
				otherTypeDescription: null
			},
			comments: this.description,
		};
	}

	private getProfessionalByType(type: EProfessionType): DocumentHealthcareProfessionalDto {
		return this.surgicalReport[this.getTypeKey(type)];
	}

	private setProfessionalByType(professional: DocumentHealthcareProfessionalDto, type: EProfessionType): void {
		this.surgicalReport[this.getTypeKey(type)] = professional;
	}

	deleteProfessional(type: EProfessionType): void {
		this.surgicalReport[this.getTypeKey(type)] = null;
		this.professional = null;
	}

	isEmpty(): boolean {
		const professional = this.surgicalReport[this.getTypeKey(this.type)];
		return !professional && !this.description;
	}

	private getTypeKey(type: EProfessionType): string {
		return this.professionalType[type] || null;
	}
}
