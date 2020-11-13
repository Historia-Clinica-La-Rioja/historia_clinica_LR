import { Component, Inject, OnInit } from '@angular/core';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { OVERLAY_DATA } from '@presentation/presentation-model';

@Component({
	selector: 'app-overlay-test',
	templateUrl: './dock-popup.component.html',
	styleUrls: ['./dock-popup.component.scss']
})
export class DockPopupComponent implements OnInit {

	constructor(
		public overlayRef: DockPopupRef,
		@Inject(OVERLAY_DATA) public data: any
	) {
	}

	ngOnInit(): void {
	}

	close(): void {
		this.overlayRef.close();
	}

	toggle(): void {
		this.overlayRef.toggle();
	}

}
