import { Component, Input } from '@angular/core';
import { Color } from '@presentation/colored-label/colored-label.component';
import { ColoredIconText } from '@presentation/components/colored-icon-text/colored-icon-text.component';
import { PresentationModule } from '@presentation/presentation.module';

@Component({
	selector: 'app-use-cases-colored-icon-text',
	templateUrl: './use-cases-colored-icon-text.component.html',
	styleUrls: ['./use-cases-colored-icon-text.component.scss'],
	standalone: true,
	imports: [PresentationModule]
})
export class UseCasesColoredIconTextComponent {

	coloredIconText: ColoredIconText;
	@Input()
	set useCase(value: USE_CASES_COLORED_ICON_TEXT) {
		this.coloredIconText = USE_CASES_ALTERNATIVES[value];
	};

	constructor() { }
}

export enum USE_CASES_COLORED_ICON_TEXT {
	INSTITUTION = 'Institución',
	CARE_LINE = 'Línea de cuidado',
	PROBLEM = 'Problema',
	PROFESSIONAL = 'Profesional',
	PRACTICE = 'Práctica/Procedimiento',
	SPECIALTY = 'Especialidad'
}

const problemIconText: ColoredIconText = {
	text: 'Problema:',
	icon: 'error_outlined',
	color: Color.GREY
}

const practiceIconText: ColoredIconText = {
	text: 'Práctica/Procedimiento:',
	icon: 'library_add',
	color: Color.GREY
}

const institutionIconText: ColoredIconText = {
	text: 'Institución:',
	icon: 'domain',
	color: Color.GREY
}

const specialtyIconText: ColoredIconText = {
	text: 'Especialidad:',
	icon: 'medical_services',
	color: Color.GREY
}

const professionalIconText: ColoredIconText = {
	text: 'Profesional:',
	icon: 'person',
	color: Color.GREY
}

const careLineIconText: ColoredIconText = {
	text: 'Línea de cuidado:',
	icon: 'diversity_1',
	color: Color.GREY
}

const USE_CASES_ALTERNATIVES = {
	[USE_CASES_COLORED_ICON_TEXT.INSTITUTION]: institutionIconText,
	[USE_CASES_COLORED_ICON_TEXT.CARE_LINE]: careLineIconText,
	[USE_CASES_COLORED_ICON_TEXT.PROBLEM]: problemIconText,
	[USE_CASES_COLORED_ICON_TEXT.PROFESSIONAL]: professionalIconText,
	[USE_CASES_COLORED_ICON_TEXT.PRACTICE]: practiceIconText,
	[USE_CASES_COLORED_ICON_TEXT.SPECIALTY]: specialtyIconText,
}
