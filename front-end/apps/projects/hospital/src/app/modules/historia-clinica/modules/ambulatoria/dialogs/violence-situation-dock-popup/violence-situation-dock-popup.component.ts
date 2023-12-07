import { Component, OnInit } from '@angular/core';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';

@Component({
	selector: 'app-violence-situation-dock-popup',
	templateUrl: './violence-situation-dock-popup.component.html',
	styleUrls: ['./violence-situation-dock-popup.component.scss']
})
export class ViolenceSituationDockPopupComponent implements OnInit {

	constructor(public dockPopupRef: DockPopupRef) { }

	ngOnInit(): void {
	}

}
