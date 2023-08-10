import { Component, Input, OnInit } from '@angular/core';
import { FormGroup, FormGroupDirective } from '@angular/forms';
import { MatDialog } from '@angular/material/dialog';
import { EquipmentTranscribeOrderPopupComponent } from '@turnos/dialogs/equipment-transcribe-order-popup/equipment-transcribe-order-popup.component';
import { medicalOrderInfo } from '@turnos/dialogs/new-appointment/new-appointment.component';

@Component({
    selector: 'app-medical-order-input',
    templateUrl: './medical-order-input.component.html',
    styleUrls: ['./medical-order-input.component.scss']
})
export class MedicalOrderInputComponent implements OnInit {
    @Input() patientMedicalOrders: medicalOrderInfo[] = [];
    @Input() patientId: number;
    @Input() disabled?: boolean;
    form: FormGroup;

    
	patientMedicalOrderTooltipDescription = '';
	isOrderTranscribed = false;
    transcribedOrder = null;

    constructor(private rootFormGroup: FormGroupDirective,
		public dialog: MatDialog,) { }

    ngOnInit(): void {
        this.form = this.rootFormGroup.control;
		this.disabled ? this.form?.get('medicalOrder')?.get('appointmentMedicalOrder').disable()
							: this.form?.get('medicalOrder')?.get('appointmentMedicalOrder').enable();
    }

    newTranscribedOrder() {
		const dialogRef = this.dialog.open(EquipmentTranscribeOrderPopupComponent, {
			width: '35%',
			autoFocus: false,
			data: {
				patientId: this.patientId,
				transcribedOrder: this.transcribedOrder
			}
		});

		dialogRef.afterClosed().subscribe(response =>{
			this.patientMedicalOrderTooltipDescription = '';
			if (response?.order){
				if (this.isOrderTranscribed) {
					this.patientMedicalOrders[this.patientMedicalOrders.length - 1] = response.order;
				} else {
					this.patientMedicalOrders.push(response.order);
				}
				this.transcribedOrder = response.transcribedOrder;
				this.form.controls.medicalOrder.get('appointmentMedicalOrder').setValue(response.order);
				this.generateTooltipOnMedicalOrderChange();
				this.isOrderTranscribed = true;
			}
		})
	}
    
	cleanInput(){
		this.form.controls.medicalOrder.get('appointmentMedicalOrder').setValue(null);
		this.patientMedicalOrderTooltipDescription = '';
	}

    generateTooltipOnMedicalOrderChange() {
		if (this.form.controls.medicalOrder.get('appointmentMedicalOrder')?.value){
			this.patientMedicalOrderTooltipDescription = this.form.controls.medicalOrder.get('appointmentMedicalOrder').value.displayText;
		}
	}
}
