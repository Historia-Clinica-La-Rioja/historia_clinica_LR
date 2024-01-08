import { Component, OnInit } from '@angular/core';
import { ViolenceSituationDockPopupComponent } from '../../dialogs/violence-situation-dock-popup/violence-situation-dock-popup.component';
import { DockPopupService } from '@presentation/services/dock-popup.service';
import { ActivatedRoute } from '@angular/router';

@Component({
	selector: 'app-violence-situations',
	templateUrl: './violence-situations.component.html',
	styleUrls: ['./violence-situations.component.scss']
})
export class ViolenceSituationsComponent implements OnInit {
patientId: number;
constructor(private readonly dockPopupService: DockPopupService, private route: ActivatedRoute) { }

ngOnInit(): void {
	this.route.paramMap.subscribe(
		(params) => {
			this.patientId = Number(params.get('idPaciente'));
		})
}

openViolenceSituationDockPopUp() {
	this.dockPopupService.open(ViolenceSituationDockPopupComponent,this.patientId);
}

}
