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
	ayudanteCount: number = 1;

	healthcareProfessionals: DocumentHealthcareProfessionalDto[] = [];

	SURGEON = EProfessionType.SURGEON;
	SURGEON_ASSISTANT = EProfessionType.SURGEON_ASSISTANT;
	ANESTHESIOLOGIST = EProfessionType.ANESTHESIOLOGIST;
	CARDIOLOGIST = EProfessionType.CARDIOLOGIST;
	SURGICAL_INSTRUMENT_TECHNICIAN = EProfessionType.SURGICAL_INSTRUMENT_TECHNICIAN;
	OBSTETRICIAN = EProfessionType.OBSTETRICIAN;
	PEDIATRICIAN = EProfessionType.PEDIATRICIAN;

	surgeon: DocumentHealthcareProfessionalDto;
	surgeonAssistant: DocumentHealthcareProfessionalDto;
	anesthesiologist: DocumentHealthcareProfessionalDto;
	cardiologist: DocumentHealthcareProfessionalDto;
	surgicalInstrumentTechnician: DocumentHealthcareProfessionalDto;
	obstetrician: DocumentHealthcareProfessionalDto;
	pediatrician: DocumentHealthcareProfessionalDto;

	constructor() {	}

	ngOnInit(): void {
		this.surgicalReport?.healthcareProfessionals.forEach(p => {
			switch (p.type) {
				case EProfessionType.SURGEON:
					this.surgeon = p;
					break;
				case EProfessionType.SURGEON_ASSISTANT:
					this.surgeonAssistant = p;
					break;
				case EProfessionType.ANESTHESIOLOGIST:
					this.anesthesiologist = p;
					break;
				case EProfessionType.CARDIOLOGIST:
					this.cardiologist = p;
					break;
				case EProfessionType.SURGICAL_INSTRUMENT_TECHNICIAN:
					this.surgicalInstrumentTechnician = p;
					break;
				case EProfessionType.OBSTETRICIAN:
					this.obstetrician = p;
					break;
				case EProfessionType.PEDIATRICIAN:
					this.pediatrician = p;
					break;
			}
		})
	}

	addAyudante() {
		this.ayudanteCount++;
	}

	professionalChange(professional: HCEHealthcareProfessionalDto, type: EProfessionType): void {
		const index = this.healthcareProfessionals.findIndex(p => p.type === type);
		if (professional && index == -1)
			this.healthcareProfessionals.push(this.mapToDocumentHealthcareProfessionalDto(professional, type));

		if (professional && index != -1)
			this.healthcareProfessionals.splice(index, 1, this.mapToDocumentHealthcareProfessionalDto(professional, type));

		if (!professional && index != -1)
			this.healthcareProfessionals.splice(index, 1);
		this.surgicalReport.healthcareProfessionals = this.healthcareProfessionals;
	}

	isEmpty(): boolean {
		return !this.surgicalReport.healthcareProfessionals.find(p =>
			p.type === EProfessionType.SURGEON ||
			p.type === EProfessionType.SURGEON_ASSISTANT ||
			p.type === EProfessionType.ANESTHESIOLOGIST ||
			p.type === EProfessionType.CARDIOLOGIST ||
			p.type === EProfessionType.SURGICAL_INSTRUMENT_TECHNICIAN ||
			p.type === EProfessionType.OBSTETRICIAN ||
			p.type === EProfessionType.PEDIATRICIAN
		);
	}

	private mapToDocumentHealthcareProfessionalDto(professional: HCEHealthcareProfessionalDto, type: EProfessionType): DocumentHealthcareProfessionalDto {
		return {
			healthcareProfessional: professional,
			type: type
		}
	}
}
