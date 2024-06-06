import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup, FormGroupDirective } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { EquipmentTranscribeOrderPopupComponent, InfoTranscribeOrderPopup } from '@turnos/dialogs/equipment-transcribe-order-popup/equipment-transcribe-order-popup.component';
import { medicalOrderInfo } from '@turnos/dialogs/new-appointment/new-appointment.component';
import { TranscribedOrderService } from '@turnos/services/transcribed-order.service';

@Component({
    selector: 'app-medical-order-input',
    templateUrl: './medical-order-input.component.html',
    styleUrls: ['./medical-order-input.component.scss']
})
export class MedicalOrderInputComponent implements OnInit {
    @Input() patientMedicalOrders: medicalOrderInfo[] = [];
    @Input() patientId: number;
    @Input() disabled?: boolean;
	@Output() selectionChange = new EventEmitter<medicalOrderInfo>();
    form: FormGroup;
	patientMedicalOrderTooltipDescription = '';
	isOrderTranscribed = false;
    transcribedOrder: InfoTranscribeOrderPopup = null;

    constructor(
		private rootFormGroup: FormGroupDirective,
		public dialog: MatDialog,
		private readonly transcribedOrderService: TranscribedOrderService) { }

    ngOnInit(): void {
        this.form = this.rootFormGroup.control;
		this.disabled ? this.form?.get('medicalOrder')?.get('appointmentMedicalOrder').disable()
							: this.form?.get('medicalOrder')?.get('appointmentMedicalOrder').enable();
		this.generateTooltipOnMedicalOrderChange();
		this.transcribedOrderService.transcribedOrder$.subscribe(transcribedOrder => {
			if (!transcribedOrder) { this.patientMedicalOrderTooltipDescription = '' }	
			this.transcribedOrder = transcribedOrder;
		})
    }

    newTranscribedOrder() {
		const dialogRef = this.dialog.open(EquipmentTranscribeOrderPopupComponent, {
			width: '35%',
			height:'651px',
			autoFocus: false,
			data: {
				patientId: this.patientId,
				transcribedOrder: this.transcribedOrder
			}
		});

		dialogRef.afterClosed().subscribe((response:ResponseTranscribedOrderPopUpInfo)  =>{
			this.patientMedicalOrderTooltipDescription = '';
			if (response?.order){
				if (this.isOrderTranscribed) {
					this.patientMedicalOrders.length ? 
						this.patientMedicalOrders[this.patientMedicalOrders.length - 1] = response.order
						: this.patientMedicalOrders.push(response.order);
				} else {
					this.patientMedicalOrders.push(response.order);
				}
				this.transcribedOrderService.setTranscribedOrder(response.transcribeOrder);
				this.form.controls.medicalOrder.get('appointmentMedicalOrder').setValue(response.order);
				this.generateTooltipOnMedicalOrderChange();
				this.isOrderTranscribed = true;
			}
		})
	}

	cleanInput(){
		this.form.controls.medicalOrder.get('appointmentMedicalOrder').setValue(null);
		this.patientMedicalOrderTooltipDescription = '';
		this.selectionChange.emit(null);
	}

    generateTooltipOnMedicalOrderChange() {
		if (this.form.controls.medicalOrder.get('appointmentMedicalOrder')?.value){
			this.patientMedicalOrderTooltipDescription = this.form.controls.medicalOrder.get('appointmentMedicalOrder').value.displayText;
		}
	}

	changeOrderSelection(){
		this.generateTooltipOnMedicalOrderChange();
		this.selectionChange.emit(this.form.controls.medicalOrder.get('appointmentMedicalOrder')?.value);
	}
}

export interface TranscribedOrderInfoEdit {
	patientId: number,
	transcribedOrder: InfoTranscribeOrderPopup
}

export interface ResponseTranscribedOrderPopUpInfo {
	transcribeOrder: InfoTranscribeOrderPopup,
	order: medicalOrderInfo
}

