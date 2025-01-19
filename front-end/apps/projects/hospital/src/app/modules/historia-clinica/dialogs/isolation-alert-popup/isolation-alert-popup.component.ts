import { Component, OnDestroy, OnInit } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';
import { IsolationAlert } from '@historia-clinica/components/isolation-alert-form/isolation-alert-form.component';
import { SaveIsolationAlertService } from '@historia-clinica/services/save-isolation-alert.service';
import { ButtonType } from '@presentation/components/button/button.component';
import { Subscription } from 'rxjs';

@Component({
	selector: 'app-isolation-alert-popup',
	templateUrl: './isolation-alert-popup.component.html',
	styleUrls: ['./isolation-alert-popup.component.scss']
})
export class IsolationAlertPopupComponent implements OnInit, OnDestroy {

	readonly RAISED = ButtonType.RAISED;
	persistSubscription: Subscription;

	constructor(
		private readonly dialogRef: MatDialogRef<IsolationAlertPopupComponent>,
		private readonly saveIsolationAlertService: SaveIsolationAlertService,
	) { }

	ngOnInit(): void {
		this.persistSubscription = this.saveIsolationAlertService.persist$.subscribe(isolationAlert => this.persisIsolationAlert(isolationAlert));
	}

	ngOnDestroy(): void {
		this.persistSubscription.unsubscribe();
	}

	submit() {
		this.saveIsolationAlertService.submitSubject.next();
	}

	persisIsolationAlert(isolationAlert: IsolationAlert) {
		this.dialogRef.close(isolationAlert);
	}

	closeDialog() {
		this.dialogRef.close();
	}

}
