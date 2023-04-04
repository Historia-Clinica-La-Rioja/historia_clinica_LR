import { Component, ViewChild } from '@angular/core';
import { NewbornDto, ObstetricEventDto } from '@api-rest/api-model';
import { FormDynamicNewBornComponent } from '../form-dynamic-new-born/form-dynamic-new-born.component';

@Component({
	selector: 'app-obstetric',
	templateUrl: './obstetric.component.html',
	styleUrls: ['./obstetric.component.scss']
})
export class ObstetricComponent {

	obstetricEvent: ObstetricEventDto;
	newborns: NewbornDto[];
	@ViewChild(FormDynamicNewBornComponent) formulario!: FormDynamicNewBornComponent;

	constructor() { }

	setObstetricEvent(obstetricEvent: ObstetricEventDto) {
		this.obstetricEvent = obstetricEvent;
	}

	getForm(): ObstetricEventDto {
		const form = { ...this.obstetricEvent, newborns: this.formulario.getForm() };
		return form
	}

	isValidForm(): boolean {
		return this.formulario.isValidForm();
	}

}
