import { Component, Input } from '@angular/core';
import { capitalizeSentence } from '@core/utils/core.utils';
import { ItemSummary, Size } from '@presentation/components/item-summary/item-summary.component';
import { PresentationModule } from '@presentation/presentation.module';

@Component({
	selector: 'app-patient-summary',
	templateUrl: './patient-summary.component.html',
	styleUrls: ['./patient-summary.component.scss'],
	standalone: true,
	imports: [PresentationModule]
})
export class PatientSummaryComponent {

	@Input() set person(p: PatientSummary) {
		this.itemSummary = {
			title: capitalizeSentence(p.fullName),
			subtitle: concatIdentifications(p.id, p.identification),
			subtitle2: concatAgeAndGender(p.gender, p.age, p.monthsOfLife),
			avatar: p.photo
		}
	}

	@Input() size?= Size.BIG;
	itemSummary: ItemSummary;

	constructor() { }

}

const concatIdentifications = (id: number, identification: { type: string, number: string }): string => {
	if (!id && !identification)
		return null;
	let idText;
	if (id) {
		idText = `ID ${id}`
	}
	let identificationText;
	if (identification) {
		identificationText = `${identification?.type} ${identification?.number ? identification.number : 'Sin información' }`
	}
	return (idText && identificationText) ? `${identificationText} - ${idText} ` : (idText || identificationText);
}

const concatAgeAndGender = (gender: string, age: number, monthsOfLife?:string): string => {
	if (!gender && !age && !monthsOfLife)
		return null;
	let ageText;
	if (age) {
		ageText = `${age} años`
	}
	let monthsOfLifeText;
	if (monthsOfLife)
	{
		monthsOfLifeText = `${monthsOfLife}`
	}
	if (age) {
		return (gender && ageText) ? `${gender} - ${ageText}` : (gender || ageText);
	}
	else{
		return (gender && monthsOfLifeText) ? `${gender} - ${monthsOfLifeText}` : (gender || monthsOfLifeText);
	}
}

export interface PatientSummary {
	fullName: string;
	identification?: {
		type: string;
		number: string;
	}
	id?: number;
	gender?: string;
	age?: number;
	monthsOfLife?: string;
	photo?: string;
}
