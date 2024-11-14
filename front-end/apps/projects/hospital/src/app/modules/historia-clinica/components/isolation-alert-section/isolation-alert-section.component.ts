import { Component, forwardRef, OnDestroy, OnInit } from '@angular/core';
import { FormControl, FormGroup, NG_VALIDATORS, NG_VALUE_ACCESSOR } from '@angular/forms';
import { AbstractCustomForm } from '@core/abstract-class/AbstractCustomForm';
import { IsolationAlertPopupComponent } from '@historia-clinica/dialogs/isolation-alert-popup/isolation-alert-popup.component';
import { DialogService, DialogWidth } from '@presentation/services/dialog.service';
import { IsolationAlert } from '../isolation-alert-form/isolation-alert-form.component';
import { Subscription, take } from 'rxjs';
import { EpisodeDiagnosesService } from '@historia-clinica/services/episode-diagnoses.service';
import { BoxMessageInformation } from '@presentation/components/box-message/box-message.component';
import { PermissionsService } from '@core/services/permissions.service';
import { ERole } from '@api-rest/api-model';

import { IsolationAlertDetail } from '../isolation-alert-detail/isolation-alert-detail.component';
import { mapIsolationAlertsToIsolationAlertsDetails } from '@historia-clinica/mappers/isolation-alerts.mapper';

const PROFESSIONAL_ROLES = [ERole.ESPECIALISTA_MEDICO, ERole.ESPECIALISTA_EN_ODONTOLOGIA, ERole.PROFESIONAL_DE_SALUD];
const BOX_MESSAGE_TITLE = 'historia-clinica.isolation-alert.without_associated_diagnosis.TITLE';
@Component({
	selector: 'app-isolation-alert-section',
	templateUrl: './isolation-alert-section.component.html',
	styleUrls: ['./isolation-alert-section.component.scss'],
	providers: [
		{
			provide: NG_VALUE_ACCESSOR,
			multi: true,
			useExisting: forwardRef(() => IsolationAlertSectionComponent),
		},
		{
			provide: NG_VALIDATORS,
			multi: true,
			useExisting: forwardRef(() => IsolationAlertSectionComponent),
		},
	],
})
export class IsolationAlertSectionComponent extends AbstractCustomForm implements OnInit, OnDestroy {

	form: FormGroup<IsolationAlertForm>;
	popUpSubscription: Subscription;
	isProfessional = false;
	isolationAlertsDetail: IsolationAlertDetail[] = [];

	readonly nurseBoxMesaggeInfo: BoxMessageInformation = {
		title: BOX_MESSAGE_TITLE,
		message: 'historia-clinica.isolation-alert.without_associated_diagnosis.SUBTITLE',
		showButtons: false
	}

	readonly professionalBoxMesaggeInfo: BoxMessageInformation = {
		title: BOX_MESSAGE_TITLE,
		showButtons: false
	}

	constructor(
		private readonly dialogService: DialogService<IsolationAlertPopupComponent>,
		readonly episodeDiagnosisService: EpisodeDiagnosesService,
		private readonly permissionService: PermissionsService
	) {
		super();
		this.createForm();
	}

	ngOnInit() {
		this.permissionService.hasContextAssignments$(PROFESSIONAL_ROLES).pipe(take(1)).subscribe(isProfessional => this.isProfessional = isProfessional);
	}

	ngOnDestroy(): void {
		this.popUpSubscription && this.popUpSubscription.unsubscribe();
	}

	createForm() {
		this.form = new FormGroup<IsolationAlertForm>({
			isolationAlerts: new FormControl<IsolationAlert[]>([])
		});
	}

	addIsolationAlert() {
		const dialogRef = this.dialogService.open(IsolationAlertPopupComponent,
			{ dialogWidth: DialogWidth.MEDIUM }, {}
		);

		this.popUpSubscription = dialogRef.afterClosed().subscribe(isolationAlert => {
			if (isolationAlert) {
				const isolationAlertsAdded = this.form.value.isolationAlerts;
				isolationAlertsAdded.push(isolationAlert);
				this.form.controls.isolationAlerts.setValue(isolationAlertsAdded);
				this.isolationAlertsDetail = mapIsolationAlertsToIsolationAlertsDetails(isolationAlertsAdded);
			}
		});
	}

	removeIsolationAlert(indexToRemove: number) {
		const isolationAlertsAdded = this.form.value.isolationAlerts;
		isolationAlertsAdded.splice(indexToRemove, 1);
		this.form.controls.isolationAlerts.setValue(isolationAlertsAdded);
		this.isolationAlertsDetail = mapIsolationAlertsToIsolationAlertsDetails(isolationAlertsAdded);
	}

	writeValue(value: {isolationAlerts: IsolationAlert[]}) {
		if (value) {
			this.form.setValue(value, { emitEvent: false });
			this.isolationAlertsDetail = mapIsolationAlertsToIsolationAlertsDetails(value.isolationAlerts);
		}
	}

}

interface IsolationAlertForm {
	isolationAlerts: FormControl<IsolationAlert[]>;
}