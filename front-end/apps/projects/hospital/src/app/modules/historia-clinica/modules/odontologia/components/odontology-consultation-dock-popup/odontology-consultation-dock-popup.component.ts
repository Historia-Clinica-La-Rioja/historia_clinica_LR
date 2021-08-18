import { Component, Inject, OnInit } from '@angular/core';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { OVERLAY_DATA } from '@presentation/presentation-model';

@Component({
	selector: 'app-odontology-consultation-dock-popup',
	templateUrl: './odontology-consultation-dock-popup.component.html',
	styleUrls: ['./odontology-consultation-dock-popup.component.scss']
})
export class OdontologyConsultationDockPopupComponent implements OnInit {

	constructor(
		@Inject(OVERLAY_DATA) public data: OdontologyConsultationData,
		public dockPopupRef: DockPopupRef
	) {

	}

	ngOnInit(): void {

	}

}

export interface OdontologyConsultationData {
	patientId: number;
}
