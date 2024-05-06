import { Component, Input } from '@angular/core';
import { IconLegend, Identifier, Position } from '@presentation/components/identifier/identifier.component';
import { PresentationModule } from '@presentation/presentation.module';

@Component({
	selector: 'app-identifier-cases',
	templateUrl: './identifier-cases.component.html',
	styleUrls: ['./identifier-cases.component.scss'],
	standalone: true,
	imports: [PresentationModule]
})
export class IdentifierCasesComponent {

	identifier: Identifier;
	@Input() position = Position.ROW;
	@Input() showLegend = false;
	@Input()
	set description(value: string) {
		this.identifier = {
			...this.identifier,
			description: value
		};
	};
	@Input()
	set identifierCase(value: IDENTIFIER_CASES) {
		const iconLegend = IDENTIFIER_CASES_ALTERNATIVES[value];

		this.identifier = {
			...this.identifier,
			iconLegend
		}
	};

	constructor() { }
}

export enum IDENTIFIER_CASES {
	INSTITUTION = 'Institución',
	CARE_LINE = 'Línea de cuidado',
	PROBLEM = 'Problema',
	PROFESSIONAL = 'Profesional',
	PRACTICE = 'Práctica / Procedimiento',
	SPECIALTY = 'Especialidades',
	DATE = 'Fecha',
	STARTDATE = 'FechaInicio',
	ENDDATE = 'FechaFin',
	HOUR = 'Hora',
	DISTRICT = 'Partido',
	SERVICE = 'Servicio',
	SECTOR = 'Sector',
	ROOM = 'Sala',
	BED = 'Cama', 
	HIERARCHICAL_UNIT = 'Unidad jerárquica',
	SCOPE = 'Ambito',
	PATIENT = 'Paciente'
}

const institution: IconLegend = {
	icon: 'domain',
	legend: 'Institución',
}

const careLine: IconLegend = {
	icon: 'diversity_1',
	legend: 'Línea de cuidado',
}

const problem: IconLegend = {
	icon: 'error_outlined',
	legend: 'Problema',
}

const professional: IconLegend = {
	icon: 'medical_information',
	legend: 'Profesional',
}

const practice: IconLegend = {
	icon: 'library_add',
	legend: 'Práctica / Procedimiento',
}

const specialty: IconLegend = {
	icon: 'medical_services',
	legend: 'Especialidades',
}

const date: IconLegend = {
	icon: 'event',
	legend: 'Fecha',
}

const startDate: IconLegend = {
	icon: 'event',
	legend: 'Fecha de inicio',
}

const endDate: IconLegend = {
	icon: 'event',
	legend: 'Fecha de fin',
}

const hour: IconLegend = {
	icon: 'schedule',
	legend: 'Hora',
}

const district: IconLegend = {
	icon: 'location_on',
	legend: 'Partido',
}

const service: IconLegend = {
	icon: 'schema',
	legend: 'Servicio',
}

const sector: IconLegend = {
	icon: 'add_location',
	legend: 'Sector',
}

const room: IconLegend = {
	icon: 'meeting_room',
	legend: 'Sala',
}

const bed: IconLegend = {
	icon: 'hotel',
	legend: 'Cama',
}

const hierarchicalUnit: IconLegend = {
	icon: 'lan',
	legend: 'Unidad jerárquica',
}

const scope: IconLegend = {
	icon: 'add_box',
	legend: 'Ambito',
}

const patient: IconLegend = {
	icon: 'person',
	legend: 'Paciente',
}

const IDENTIFIER_CASES_ALTERNATIVES = {
	[IDENTIFIER_CASES.INSTITUTION]: institution,
	[IDENTIFIER_CASES.CARE_LINE]: careLine,
	[IDENTIFIER_CASES.PROBLEM]: problem,
	[IDENTIFIER_CASES.PROFESSIONAL]: professional,
	[IDENTIFIER_CASES.PRACTICE]: practice,
	[IDENTIFIER_CASES.SPECIALTY]: specialty,
	[IDENTIFIER_CASES.DATE]: date,
	[IDENTIFIER_CASES.STARTDATE]: startDate,
	[IDENTIFIER_CASES.ENDDATE]: endDate,
	[IDENTIFIER_CASES.HOUR]: hour,
	[IDENTIFIER_CASES.DISTRICT]: district,
	[IDENTIFIER_CASES.SERVICE]: service,
	[IDENTIFIER_CASES.SECTOR]: sector,
	[IDENTIFIER_CASES.ROOM]: room,
	[IDENTIFIER_CASES.BED]: bed,
	[IDENTIFIER_CASES.HIERARCHICAL_UNIT]: hierarchicalUnit,
	[IDENTIFIER_CASES.SCOPE]: scope,
	[IDENTIFIER_CASES.PATIENT]: patient,
}
