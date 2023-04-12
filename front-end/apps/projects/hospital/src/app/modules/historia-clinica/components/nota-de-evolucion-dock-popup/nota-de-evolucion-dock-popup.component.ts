import { Component} from '@angular/core';
import { FormBuilder } from '@angular/forms';
import { DockPopUpHeader } from '@presentation/components/dock-popup/dock-popup.component';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';

@Component({
	selector: 'app-nota-de-evolucion-dock-popup',
	templateUrl: './nota-de-evolucion-dock-popup.component.html',
	styleUrls: ['./nota-de-evolucion-dock-popup.component.scss']
})
export class NotaDeEvolucionDockPopupComponent {

	readonly header: DockPopUpHeader = {
		title: 'Nota de evolución'
	}

	form = this.formBuilder.group({
		specialty: [],
		motivo: [],
		diagnoticos: [],
		evolucion: [],
		antropometricos: [],
		antecedentesFamiliares: [],
		medicaciones: [],
		procedimientos: [],
		factoresDeRiesgo: [],
		alergias: []
	});

	disableConfirmButton = false;

	constructor(
		public dockPopupRef: DockPopupRef,
		private formBuilder: FormBuilder,
	) { }

	save() {
		console.log('Formulario: ', this.form.value);
	}

}
