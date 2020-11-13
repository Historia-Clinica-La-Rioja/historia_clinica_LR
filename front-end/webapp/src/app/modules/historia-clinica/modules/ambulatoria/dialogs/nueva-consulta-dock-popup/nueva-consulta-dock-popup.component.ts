import { Component, OnInit } from '@angular/core';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';

@Component({
	selector: 'app-nueva-consulta-dock-popup',
	templateUrl: './nueva-consulta-dock-popup.component.html',
	styleUrls: ['./nueva-consulta-dock-popup.component.scss']
})
export class NuevaConsultaDockPopupComponent implements OnInit {

	constructor(
		public dockPopupRef: DockPopupRef
	) {
	}

	ngOnInit(): void {
	}

}
