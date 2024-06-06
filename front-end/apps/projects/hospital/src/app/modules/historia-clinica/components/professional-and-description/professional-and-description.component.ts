import { Component, Input, OnInit } from '@angular/core';
import { DocumentHealthcareProfessionalDto, EProfessionType, HCEHealthcareProfessionalDto, HealthcareProfessionalDto, SurgicalReportDto } from '@api-rest/api-model';

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

	constructor() { }

	ngOnInit(): void {
		this.professional = this.surgicalReport.healthcareProfessionals.find(p => p.type === this.type);
		this.description = this.professional?.comments;
	}

	changeProfessional(professional: HCEHealthcareProfessionalDto): void {
		if (professional){
			this.professional = this.mapToDocumentHealthcareProfessionalDto(professional);
			this.addProfessional(this.professional, this.type);
		}
		else
			this.deleteProfessional(this.type);
	}

	changeDescription(description: string): void {
		if (this.professional) {
			this.professional.comments = description
			this.addProfessional(this.professional, this.type);
		}
	}

	private mapToDocumentHealthcareProfessionalDto(professional: HCEHealthcareProfessionalDto): DocumentHealthcareProfessionalDto {
		return {
			healthcareProfessional: professional,
			type: this.type,
		}
	}

	addProfessional(professional: DocumentHealthcareProfessionalDto, type: EProfessionType): void {
		professional.type = type;
		if (!this.surgicalReport.healthcareProfessionals.length)
			this.surgicalReport.healthcareProfessionals.push(professional)
		else {
			const index = this.surgicalReport.healthcareProfessionals.findIndex(p => p.type === type);
			if (professional && index == -1)
				this.surgicalReport.healthcareProfessionals.push(professional);

			if (professional && index != -1)
				this.surgicalReport.healthcareProfessionals.splice(index, 1, professional);

			if (!professional && index != -1)
				this.surgicalReport.healthcareProfessionals.splice(index, 1);
		}
	}

	deleteProfessional(type: EProfessionType): void {
		const index = this.surgicalReport.healthcareProfessionals.findIndex(p => p.type === type);
		this.professional = null;
		this.surgicalReport.healthcareProfessionals.splice(index, 1);
	}

	isEmpty(): boolean {
		return !this.surgicalReport.healthcareProfessionals.find(p => p.type === this.type) && !this.description;
	}
}
