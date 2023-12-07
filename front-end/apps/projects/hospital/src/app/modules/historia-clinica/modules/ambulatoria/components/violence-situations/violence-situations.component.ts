import { Component, OnInit } from '@angular/core';
import { ViolenceSituationDockPopupComponent } from '../../dialogs/violence-situation-dock-popup/violence-situation-dock-popup.component';
import { DockPopupService } from '@presentation/services/dock-popup.service';

@Component({
	selector: 'app-violence-situations',
	templateUrl: './violence-situations.component.html',
	styleUrls: ['./violence-situations.component.scss']
})
export class ViolenceSituationsComponent implements OnInit {

	constructor(private readonly dockPopupService: DockPopupService) { }

	ngOnInit(): void {
	}

	openViolenceSituationDockPopUp() {
		this.dockPopupService.open(ViolenceSituationDockPopupComponent);
	}

}
