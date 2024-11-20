import { Component, Input } from '@angular/core';
import { Color } from '@presentation/colored-label/colored-label.component';
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
		const iconLegend = {
			...IDENTIFIER_CASES_ALTERNATIVES[value],
		};

		this.identifier = {
			...this.identifier,
			iconLegend
		}
	};
	@Input() color: Color;

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
	DOCTOR_OFFICE = 'Consultorio',
	BED = 'Cama',
	HIERARCHICAL_UNIT = 'Unidad jerárquica',
	SCOPE = 'Ambito',
	PATIENT = 'Paciente',
	REASON = 'Motivo',
	EMERGENCY_CARE_TYPE = 'Tipo de guardia',
	SHOCKROOM = 'Shockroom',
	ROOM_V2 = 'Habitación',
	ERROR = 'Tipo de estudio',
	PIN_DROP = 'Requiere translado',
	LOCAL_HOSPITAL = 'Sector ordenes',
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

const doctor_office: IconLegend = {
	icon: 'meeting_room',
	legend: 'Consultorio',
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
	legend: 'Ámbito',
}

const patient: IconLegend = {
	icon: 'person',
	legend: 'Paciente',
}

const reason: IconLegend = {
	icon: 'announcement',
	legend: 'Motivo',
}

const emergencyCareType: IconLegend = {
	icon: 'add_box',
	legend: 'Tipo de guardia',
}

const shockroom: IconLegend = {
	icon: 'meeting_room',
	legend: 'Shockroom',
}

const room_v2: IconLegend = {
	icon: 'meeting_room',
	legend: 'Habitación',
}

const error: IconLegend = {
	icon: 'error',
	legend: 'Tipo de estudio',
}

const pin_drop: IconLegend = {
	icon: 'pin_drop',
	legend: 'Requiere translado',
}

const local_hospital: IconLegend = {
	icon: 'local_hospital',
	legend: 'Sector',
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
	[IDENTIFIER_CASES.DOCTOR_OFFICE]: doctor_office,
	[IDENTIFIER_CASES.BED]: bed,
	[IDENTIFIER_CASES.HIERARCHICAL_UNIT]: hierarchicalUnit,
	[IDENTIFIER_CASES.SCOPE]: scope,
	[IDENTIFIER_CASES.PATIENT]: patient,
	[IDENTIFIER_CASES.REASON]: reason,
	[IDENTIFIER_CASES.EMERGENCY_CARE_TYPE]: emergencyCareType,
	[IDENTIFIER_CASES.SHOCKROOM]: shockroom,
	[IDENTIFIER_CASES.ROOM_V2]: room_v2,
	[IDENTIFIER_CASES.ERROR]: error,
	[IDENTIFIER_CASES.PIN_DROP]: pin_drop,
	[IDENTIFIER_CASES.LOCAL_HOSPITAL]: local_hospital,
}
