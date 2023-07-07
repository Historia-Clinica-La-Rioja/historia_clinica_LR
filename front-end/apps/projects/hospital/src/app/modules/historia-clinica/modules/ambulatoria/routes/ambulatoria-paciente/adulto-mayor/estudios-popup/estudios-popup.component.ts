import { Component } from '@angular/core';
import { MatDialogRef } from '@angular/material/dialog';

@Component({
  selector: 'app-estudios-popup',
  templateUrl: './estudios-popup.component.html',
  styleUrls: ['./estudios-popup.component.scss']
})
export class EstudiosPopupComponent {
  selectedOption: string | undefined;

  constructor(private dialogRef: MatDialogRef<EstudiosPopupComponent>) {}

  cerrarPopup() {
    if (this.dialogRef) {
      this.dialogRef.close();
    }
  }

}
