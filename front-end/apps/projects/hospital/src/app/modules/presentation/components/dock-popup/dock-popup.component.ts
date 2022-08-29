import { Component, Input } from '@angular/core';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';

@Component({
	selector: 'app-dock-popup',
	templateUrl: './dock-popup.component.html',
	styleUrls: ['./dock-popup.component.scss']
})
export class DockPopupComponent {

	@Input() dockPopupRef: DockPopupRef;
	@Input() header: DockPopUpHeader;
	@Input() style: DockPopUpStyle = {
		cssClassContent: 'content',
		cssClassBody: 'body',
		cssClassActions : 'actions',
	};
	@Input() actions: DockPopUpActions = {
		lockToggle: false,
		showButtonClose: true,
		showButtonChevron: false,
		showActions: true
	};

	constructor() { }

	close(): void {
		this.dockPopupRef.close();
	}

	toggleWithoutHeader() {
		this.dockPopupRef.toggleWithoutHeader();
	}

	toggle(): void {
		if (!this.actions.lockToggle)
			this.dockPopupRef.toggle();
	}

}

export interface DockPopUpHeader {
	title: string;
	matIcon?: string;
}

export interface DockPopUpActions {
	lockToggle?: boolean;
	showButtonClose?: boolean;
	showButtonChevron?: boolean;
	showActions?: boolean;
}

export interface DockPopUpStyle {
	cssClassContent: string;
	cssClassBody: string;
	cssClassActions: string;
}