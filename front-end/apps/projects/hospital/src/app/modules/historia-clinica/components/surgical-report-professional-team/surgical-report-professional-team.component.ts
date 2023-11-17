import { Component, Input, OnInit } from '@angular/core';
import { FormBuilder, FormControl } from '@angular/forms';
import { ProfessionalDto } from '@api-rest/api-model';

@Component({
	selector: 'app-surgical-report-professional-team',
	templateUrl: './surgical-report-professional-team.component.html',
	styleUrls: ['./surgical-report-professional-team.component.scss']
})
export class SurgicalReportProfessionalTeamComponent implements OnInit {

	form = this.formBuilder.group({
		cirujano: new FormControl<ProfessionalDto | null>(null),
		ayudantes: new FormControl<ProfessionalDto[] | null>(null),
		anestesiologo: new FormControl<ProfessionalDto | null>(null),
		cardiologo: new FormControl<ProfessionalDto | null>(null),
		instrumentador: new FormControl<ProfessionalDto | null>(null),
		obstetra: new FormControl<ProfessionalDto | null>(null),
		pediatra: new FormControl<ProfessionalDto | null>(null),
	});

	@Input() professionals: ProfessionalDto[];

	ayudanteCount: number = 1;

	constructor(
		private formBuilder: FormBuilder
	) {	}

	ngOnInit(): void { }

	addAyudante() {
		this.ayudanteCount++;
	}
}
