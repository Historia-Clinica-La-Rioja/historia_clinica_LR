import { Component, Inject, OnInit } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { SnackBarService } from '@presentation/services/snack-bar.service';
import { AppointmentsFacadeService } from '../../services/appointments-facade.service';
import { EquipmentAppointmentsFacadeService } from '@turnos/modules/image-network/services/equipment-appointments-facade.service';

@Component({
  selector: 'app-cancel-appointment',
  templateUrl: './cancel-appointment.component.html',
  styleUrls: ['./cancel-appointment.component.scss']
})
export class CancelAppointmentComponent implements OnInit {

	formMotivo: FormGroup;

	constructor(
		public dialogRef: MatDialogRef<CancelAppointmentComponent>,
		@Inject(MAT_DIALOG_DATA) public data: {
			appointmentId: number,
			imageNetworkAppointment?: boolean
		},
		private readonly formBuilder: FormBuilder,
		private readonly snackBarService: SnackBarService,
		private readonly appointmentsFacade: AppointmentsFacadeService,
		private readonly equipmentAppointmensFacade: EquipmentAppointmentsFacadeService,
	) {
	}

	ngOnInit(): void {
		this.formMotivo = this.formBuilder.group({
			motivo: ['', Validators.required]
		});
	}

	cancel(): void {
		if (this.formMotivo.controls.motivo.valid) {
			if (this.data.imageNetworkAppointment){
				this.callEquimentAppointmentsFacade();
			} else {
				this.callAppointmentsFacade();
			}
		}

	}

	callEquimentAppointmentsFacade(){
		this.equipmentAppointmensFacade.cancelAppointment(this.data.appointmentId, this.formMotivo.controls.motivo.value).subscribe(() => {
			this.dialogRef.close(true);
			this.snackBarService.showSuccess('turnos.cancel.SUCCESS');
		}, _ => {
			this.snackBarService.showError(`Error al cancelar turno`);
		});
	}

	callAppointmentsFacade(){
		this.appointmentsFacade.cancelAppointment(this.data.appointmentId, this.formMotivo.controls.motivo.value).subscribe(() => {
			this.dialogRef.close(true);
			this.snackBarService.showSuccess('turnos.cancel.SUCCESS');
		}, _ => {
			this.snackBarService.showError(`Error al cancelar turno`);
		});
	}

}
