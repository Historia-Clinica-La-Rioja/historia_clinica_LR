import { Component, forwardRef, OnDestroy } from '@angular/core';
import { FormControl, FormGroup, NG_VALIDATORS, NG_VALUE_ACCESSOR } from '@angular/forms';
import { AbstractCustomForm } from '@core/abstract-class/AbstractCustomForm';
import { IsolationAlertPopupComponent } from '@historia-clinica/dialogs/isolation-alert-popup/isolation-alert-popup.component';
import { DialogService, DialogWidth } from '@presentation/services/dialog.service';
import { IsolationAlert } from '../isolation-alert-form/isolation-alert-form.component';
import { Subscription } from 'rxjs';

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
export class IsolationAlertSectionComponent extends AbstractCustomForm implements OnDestroy {

	form: FormGroup<IsolationAlertForm>;
	popUpSubscription: Subscription;

	constructor(
		private readonly dialogService: DialogService<IsolationAlertPopupComponent>,
	) {
		super();
		this.createForm();
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
			}
		});
	}

}

interface IsolationAlertForm {
	isolationAlerts: FormControl<IsolationAlert[]>;
}