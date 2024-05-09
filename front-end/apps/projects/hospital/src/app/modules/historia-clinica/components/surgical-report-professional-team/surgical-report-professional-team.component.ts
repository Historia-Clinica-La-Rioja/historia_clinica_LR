import { Component, Input, OnInit } from '@angular/core';
import { DocumentHealthcareProfessionalDto, EProfessionType, HCEHealthcareProfessionalDto, ProfessionalDto, SurgicalReportDto } from '@api-rest/api-model';

@Component({
	selector: 'app-surgical-report-professional-team',
	templateUrl: './surgical-report-professional-team.component.html',
	styleUrls: ['./surgical-report-professional-team.component.scss']
})

export class SurgicalReportProfessionalTeamComponent implements OnInit {

	@Input() professionals: ProfessionalDto[];
	@Input() surgicalReport: SurgicalReportDto;

	healthcareProfessionals: DocumentHealthcareProfessionalDto[] = [];
	surgeon: DocumentHealthcareProfessionalDto;
	OTHER = EProfessionType.OTHER;

	mockedProfessional: DocumentHealthcareProfessionalDto = {
		healthcareProfessional: {
			id: 1,
			licenseNumber: "1111111",
			person: {
				birthDate: "",
				fullName: "Juan Carlos Rodriguez",
				id: 1,
				identificationNumber: "37123456",
			}
		},
		profession: {
			type: EProfessionType.ANESTHESIOLOGIST,
			typeDescription: null
		}
	};

	mockedProfessional2: DocumentHealthcareProfessionalDto = {
		healthcareProfessional: {
			id: 2,
			licenseNumber: "2222222",
			person: {
				birthDate: "",
				fullName: "Mariano Gutierrez",
				id: 2,
				identificationNumber: "18123456",
			}
		},
		profession: {
			type: EProfessionType.OTHER,
			typeDescription: "AYUDANTE"
		}
	};

	constructor() { }

	ngOnInit(): void {
		this.surgeon = this.surgicalReport.healthcareProfessionals.find(p => p.profession.type === EProfessionType.SURGEON)
	}

	addProfessional(): void {
		this.healthcareProfessionals.push(this.mockedProfessional);
		this.surgicalReport.healthcareProfessionals.push(this.mockedProfessional);
		this.healthcareProfessionals.push(this.mockedProfessional2);
		this.surgicalReport.healthcareProfessionals.push(this.mockedProfessional2);
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
				typeDescription: description
			}
		}
	}
}
