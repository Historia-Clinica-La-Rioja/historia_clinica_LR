import { Component, EventEmitter, Inject, Input, OnInit, Output } from '@angular/core';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';

@Component({
	selector: 'app-dock-popup',
	templateUrl: './dock-popup.component.html',
	styleUrls: ['./dock-popup.component.scss']
})
export class DockPopupComponent implements OnInit {

	@Input() dockPopupRef: DockPopupRef;
	@Input() title: string;

	constructor(
	) {
	}

	ngOnInit(): void {
	}

	close(): void {
		this.dockPopupRef.close();
	}

	toggle(): void {
		this.dockPopupRef.toggle();
	}

}
