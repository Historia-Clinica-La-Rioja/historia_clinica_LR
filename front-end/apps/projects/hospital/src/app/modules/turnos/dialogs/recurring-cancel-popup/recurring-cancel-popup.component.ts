import { Component, Inject } from '@angular/core';
import { MatDialogRef, MAT_DIALOG_DATA } from '@angular/material/dialog';
import { APPOINTMENT_CANCEL_OPTIONS } from '@turnos/constants/appointment';

@Component({
  selector: 'app-recurring-cancel-popup',
  templateUrl: './recurring-cancel-popup.component.html',
  styleUrls: ['./recurring-cancel-popup.component.scss']
})
export class RecurringCancelPopupComponent {

  options: RecurringCancelOption[] = [
    {
      title: 'turnos.appointment.cancel-dialog.CURRENT_TURN',
      value: APPOINTMENT_CANCEL_OPTIONS.CURRENT_TURN
    },
    {
      title: 'turnos.appointment.cancel-dialog.CURRENT_AND_NEXTS_TURNS',
      value: APPOINTMENT_CANCEL_OPTIONS.CURRENT_AND_NEXTS_TURNS
    },
    {
      title: 'turnos.appointment.cancel-dialog.ALL_TURNS',
      value: APPOINTMENT_CANCEL_OPTIONS.ALL_TURNS
    }
  ];

  constructor(public dialogRef: MatDialogRef<RecurringCancelPopupComponent>,
              @Inject(MAT_DIALOG_DATA) public data: {
                title: string
              }) { }

  cancel(value: number) {
    this.dialogRef.close(value);
  }
}

export interface RecurringCancelOption {
  title: string,
  value: APPOINTMENT_CANCEL_OPTIONS
}