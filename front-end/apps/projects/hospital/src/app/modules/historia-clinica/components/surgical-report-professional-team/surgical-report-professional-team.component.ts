import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { DocumentHealthcareProfessionalDto, EProfessionType, HCEHealthcareProfessionalDto, HealthcareProfessionalDto } from '@api-rest/api-model';

@Component({
	selector: 'app-surgical-report-professional-team',
	templateUrl: './surgical-report-professional-team.component.html',
	styleUrls: ['./surgical-report-professional-team.component.scss']
})

export class SurgicalReportProfessionalTeamComponent implements OnInit {

	@Input() professionals: HealthcareProfessionalDto[];
	@Output() healthcareProfessionalsChange = new EventEmitter();

	ayudanteCount: number = 1;

	healthcareProfessionals: DocumentHealthcareProfessionalDto[] = [];

	SURGEON = EProfessionType.SURGEON;
	SURGEON_ASSISTANT = EProfessionType.SURGEON_ASSISTANT;
	ANESTHESIOLOGIST = EProfessionType.ANESTHESIOLOGIST;
	CARDIOLOGIST = EProfessionType.CARDIOLOGIST;
	SURGICAL_INSTRUMENT_TECHNICIAN = EProfessionType.SURGICAL_INSTRUMENT_TECHNICIAN;
	OBSTETRICIAN = EProfessionType.OBSTETRICIAN;
	PEDIATRICIAN = EProfessionType.PEDIATRICIAN;

	constructor() { }

	ngOnInit(): void { }

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
		this.healthcareProfessionalsChange.emit(this.healthcareProfessionals);
	}

	private mapToDocumentHealthcareProfessionalDto(professional: HCEHealthcareProfessionalDto, type: EProfessionType): DocumentHealthcareProfessionalDto {
		return {
			healthcareProfessional: professional,
			type: type
		}
	}
}
