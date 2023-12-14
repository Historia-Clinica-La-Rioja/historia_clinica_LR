import { Component, Input, OnInit } from '@angular/core';
import { DocumentHealthcareProfessionalDto, EProfessionType, HCEHealthcareProfessionalDto, HealthcareProfessionalDto, SurgicalReportDto } from '@api-rest/api-model';

@Component({
	selector: 'app-surgical-report-professional-team',
	templateUrl: './surgical-report-professional-team.component.html',
	styleUrls: ['./surgical-report-professional-team.component.scss']
})

export class SurgicalReportProfessionalTeamComponent implements OnInit {

	@Input() professionals: HealthcareProfessionalDto[];
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

	loading = false;

	constructor() {	}

	ngOnInit(): void {
		this.loading = true;
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
		this.loading = false;
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

	private mapToDocumentHealthcareProfessionalDto(professional: HCEHealthcareProfessionalDto, type: EProfessionType): DocumentHealthcareProfessionalDto {
		return {
			healthcareProfessional: professional,
			type: type
		}
	}
}
