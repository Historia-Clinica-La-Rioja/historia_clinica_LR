import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { DocumentHealthcareProfessionalDto, HCEHealthcareProfessionalDto, HealthcareProfessionalDto } from '@api-rest/api-model';

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

	@Output() professionalChange = new EventEmitter();

	description: string;
	professional: DocumentHealthcareProfessionalDto;

	constructor() { }

	ngOnInit(): void {
	}

	changeProfessional(professional: HCEHealthcareProfessionalDto): void {
		this.professional = this.mapToDocumentHealthcareProfessionalDto(professional);
		this.professionalChange.emit(this.professional);
	}

	changeDescription(description: string): void {
		this.professional.comments = description
		this.professionalChange.emit(this.professional);
	}

	private mapToDocumentHealthcareProfessionalDto(professional: HCEHealthcareProfessionalDto): DocumentHealthcareProfessionalDto {
		return {
			healthcareProfessional: professional,
			type: undefined,
		}
	}
}
