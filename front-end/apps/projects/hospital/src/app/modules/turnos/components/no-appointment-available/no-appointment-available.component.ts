
import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { DiscardWarningComponent } from '@presentation/dialogs/discard-warning/discard-warning.component';
import { TabsLabel } from '@turnos/constants/tabs';
import { TabsService } from '@turnos/services/tabs.service';
@Component({
  selector: 'app-no-appointment-available',
  templateUrl: './no-appointment-available.component.html',
  styleUrls: ['./no-appointment-available.component.scss']
})
export class NoAppointmentAvailableComponent implements OnInit {
  @Output() preloadData = new EventEmitter<boolean>();
  @Output() registerUnsatisfiedDemand = new EventEmitter<boolean>();

  constructor(
    private readonly tabsService: TabsService, private dialog: MatDialog) { }

  ngOnInit(): void {
  }

  redirectToSearchInCareNetwork() {
    this.preloadData.emit(true);
    this.tabsService.setTab(TabsLabel.CARE_NETWORK);
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
        this.registerUnsatisfiedDemand.emit(true);
			}
		});
  }
}
