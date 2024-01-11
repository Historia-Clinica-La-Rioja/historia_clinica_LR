import { Component, Inject } from '@angular/core';
import { ViolenceReportDto } from '@api-rest/api-model';
import { ViolenceReportService } from '@api-rest/services/violence-report.service';
import { OVERLAY_DATA } from '@presentation/presentation-model';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Observable, of } from 'rxjs';

@Component({
	selector: 'app-violence-situation-dock-popup',
	templateUrl: './violence-situation-dock-popup.component.html',
	styleUrls: ['./violence-situation-dock-popup.component.scss']
})
export class ViolenceSituationDockPopupComponent {
	confirmForm: Observable<boolean> = of(false);
	newViolenceSituation: ViolenceReportDto;
	viewError = false;
	constructor(@Inject(OVERLAY_DATA) public patientId: number,public dockPopupRef: DockPopupRef,
	 private readonly violenceReportService: ViolenceReportService, private snackbarServices: SnackBarService) {
		this.newViolenceSituation = {
			aggressorData: null,
			episodeData: null,
			implementedActions: null,
			observation: '',
			victimData: null,
		}
	}


	setPersonInformation(event) {
		this.newViolenceSituation.victimData = event;
	}

	setImplementedActionsInfo(event) {
		this.newViolenceSituation.implementedActions = event;
	}

	setRelevantInformation(event) {
		this.newViolenceSituation.observation = event.observations;
	}

	setAggressorsListInfo(event) {
		this.newViolenceSituation.aggressorData = event;
	}

	setViolenceEpisodeInfo(event) {
		this.newViolenceSituation.episodeData = event;
	}

	confirmViolenceForm(){
		this.confirmForm = of(true);
		setTimeout(() => {
			if(this.isValidForm()){
				this.viewError = false;
				this.saveSituationViolence();
			}else{
				this.viewError= true;
			}
		}, 1000);
		
	}

	isValidForm(): boolean{
		return (this.newViolenceSituation.aggressorData.length && this.newViolenceSituation.episodeData !== null && this.newViolenceSituation.implementedActions !== null && this.newViolenceSituation.victimData !== null)
	}

	saveSituationViolence() {
		this.violenceReportService.saveNewViolenceReport(this.newViolenceSituation, this.patientId).subscribe(res=>{
		this.snackbarServices.showSuccess('ambulatoria.paciente.violence-situations.dialog.SUCCESS')
		this.dockPopupRef.close();
	})
	}
}
