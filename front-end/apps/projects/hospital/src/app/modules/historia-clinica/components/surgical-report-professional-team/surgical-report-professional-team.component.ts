import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { ProfessionalDto } from '@api-rest/api-model';
@Component({
	selector: 'app-surgical-report-professional-team',
	templateUrl: './surgical-report-professional-team.component.html',
	styleUrls: ['./surgical-report-professional-team.component.scss']
})
export class SurgicalReportProfessionalTeamComponent implements OnInit {

	@Input() professionals: ProfessionalDto[];
	@Output() healthcareProfessionalsChange = new EventEmitter();

	ayudanteCount: number = 1;

	healthcareProfessionals: ProfessionalDto[] = [];

	constructor() { }

	ngOnInit(): void { }

	addAyudante() {
		this.ayudanteCount++;
	}

	professionalChange(professional: ProfessionalDto, type: string): void {
		this.healthcareProfessionals.push(professional);
		this.healthcareProfessionalsChange.emit(this.healthcareProfessionals);
	}
}
