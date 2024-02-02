import { Component, Inject, OnInit } from '@angular/core';
import { ViolenceReportDto } from '@api-rest/api-model';
import { ViolenceReportFacadeService } from '@api-rest/services/violence-report-facade.service';
import { ViolenceReportService } from '@api-rest/services/violence-report.service';
import { OVERLAY_DATA } from '@presentation/presentation-model';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { Observable, finalize, of } from 'rxjs';
import { ViolenceAggressorsNewConsultationService } from '../../services/violence-aggressors-new-consultation.service';
import { ViolenceModalityNewConsultationService } from '../../services/violence-modality-new-consultation.service';
import { ViolenceSituationsNewConsultationService } from '../../services/violence-situations-new-consultation.service';
import { ButtonType } from '@presentation/components/button/button.component';

@Component({
	selector: 'app-violence-situation-dock-popup',
	templateUrl: './violence-situation-dock-popup.component.html',
	styleUrls: ['./violence-situation-dock-popup.component.scss']
})
export class ViolenceSituationDockPopupComponent implements OnInit{
	confirmForm: Observable<boolean> = of(false);
	newViolenceSituation: ViolenceReportDto;
	viewError = false;
	buttonRaised: ButtonType = ButtonType.RAISED;
	isSaving: boolean = false;

	constructor(@Inject(OVERLAY_DATA) public data,
				public dockPopupRef: DockPopupRef,
				private readonly violenceReportService: ViolenceReportService,
				private snackbarServices: SnackBarService,
				private readonly violenceAggressorsNewConsultationService: ViolenceAggressorsNewConsultationService,
				private readonly violenceSituationService: ViolenceSituationsNewConsultationService,
				private readonly violenceModalityService: ViolenceModalityNewConsultationService,
	 			private readonly violenceReportFacadeService: ViolenceReportFacadeService) {
		this.newViolenceSituation = {
			aggressorData: null,
			episodeData: null,
			implementedActions: null,
			observation: '',
			victimData: null,
		}
	}

	ngOnInit() {
		this.dockPopupRef.afterClosed().subscribe(close =>{
			this.resetServices();
		});
		this.setViolenceSituation();
	}

	setViolenceSituation() {
		if (this.data.data.situationId)
			this.violenceReportFacadeService.setViolenceSituation(this.data.data.situationId, this.data.data.patientId);
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
		this.isSaving = true;
		this.confirmForm = of(true);
		setTimeout(() => {
			if(this.isValidForm()){
				this.viewError = false;
				this.saveSituationViolence();
			}else{
				this.viewError= true;
				this.isSaving = false;
			}
		}, 1000);

	}

	isValidForm(): boolean{
		return (this.newViolenceSituation.aggressorData.length && this.newViolenceSituation.episodeData !== null && this.newViolenceSituation.implementedActions !== null && this.newViolenceSituation.victimData !== null)
	}

	saveSituationViolence() {
		if (this.data.data.situationId) {
			this.violenceReportService.evolveViolenceReport(this.newViolenceSituation, this.data.data.patientId, this.data.data.situationId)
				.pipe(finalize(() => this.isSaving = false))
				.subscribe({
					next: (_) => this.success()
				});
		} else {
			this.violenceReportService.saveNewViolenceReport(this.newViolenceSituation, this.data.data.patientId)
				.pipe(finalize(() => this.isSaving = false))
				.subscribe({
					next: (_) => this.success()
				});
		}

	}

	success() {
		this.violenceReportFacadeService.setAllPatientViolenceSituations(this.data.data.patientId, true);
		this.violenceReportFacadeService.setEvolutions(this.data.data.patientId, null);
		this.snackbarServices.showSuccess('ambulatoria.paciente.violence-situations.dialog.SUCCESS')
		this.dockPopupRef.close();
	}

	resetServices() {
		this.violenceAggressorsNewConsultationService.reset();
		this.violenceModalityService.reset();
		this.violenceSituationService.reset();
	}
}
