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
			subtitle2: concatAgeAndGender(p.gender, p.age),
			avatar: p.photo
		}
	}

	@Input() size?= Size.BIG;
	itemSummary: ItemSummary;

	constructor() { }

}

const concatIdentifications = (id: number, identification: { type: string, number: number }): string => {
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
	return (idText && identificationText) ? `${idText} - ${identificationText}` : (idText || identificationText);
}

const concatAgeAndGender = (gender: string, age: number): string => {
	if (!gender && !age)
		return null;
	let ageText;
	if (age) {
		ageText = `${age} años`
	}
	return (gender && ageText) ? `${gender} - ${ageText}` : (gender || ageText);
}

export interface PatientSummary {
	fullName: string;
	identification?: {
		type: string;
		number: number;
	}
	id?: number;
	gender?: string;
	age?: number;
	photo?: string;
}
