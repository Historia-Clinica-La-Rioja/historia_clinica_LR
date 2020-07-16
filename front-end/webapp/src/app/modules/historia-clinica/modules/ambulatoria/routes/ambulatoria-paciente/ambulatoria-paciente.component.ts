import { Component, OnInit } from '@angular/core';
import { PatientBasicData } from '@presentation/components/patient-card/patient-card.component';

@Component({
	selector: 'app-ambulatoria-paciente',
	templateUrl: './ambulatoria-paciente.component.html',
	styleUrls: ['./ambulatoria-paciente.component.scss']
})
export class AmbulatoriaPacienteComponent implements OnInit {

	mockedPatient: PatientBasicData = {
		id: 91218,
		firstName: 'Ivael',
		lastName: 'Tercero',
		gender: 'Masculino',
		age: 2,
	};

	constructor() {}

	ngOnInit(): void {
	}


}
