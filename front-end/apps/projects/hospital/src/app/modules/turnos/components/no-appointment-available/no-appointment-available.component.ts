
import { Component, EventEmitter, Input, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DiscardWarningComponent } from '@presentation/dialogs/discard-warning/discard-warning.component';
@Component({
  selector: 'app-no-appointment-available',
  templateUrl: './no-appointment-available.component.html',
  styleUrls: ['./no-appointment-available.component.scss']
})
export class NoAppointmentAvailableComponent {
  @Output() preloadData = new EventEmitter<boolean>();
  @Output() registerUnsatisfiedDemand = new EventEmitter<boolean>();
  @Input() set reset (value:boolean){
    this.isRegisterButtonDisabled = false;
  }
  @Input() redirectionDisabled?: boolean;
  isRegisterButtonDisabled = false;
  constructor(private dialog: MatDialog) { }

  redirectToSearchInCareNetwork() {
    this.preloadData.emit(true);
  }

  openRegisterUnsatisfiedDemand() {
   let dialogRef = this.dialog.open(DiscardWarningComponent, {
      data: {
        title: 'turnos.home.messages.TITLE_REGISTER_UNSATISFIED_DEMAND',
        content: 'turnos.home.messages.MESSAGE_REGISTER_UNSATISFIED_DEMAND',
        okButtonLabel: 'buttons.YES_REGISTER',
        cancelButtonLabel: 'buttons.NO_CANCEL',
        buttonClose: true,

      },
      disableClose: true,
      width: '35%',
      autoFocus: false
    })
    dialogRef.afterClosed().subscribe(confirmSaveRegister => {
			if (confirmSaveRegister) {
        this.isRegisterButtonDisabled = true;
        this.registerUnsatisfiedDemand.emit(true);
			}
		});
  }
}
