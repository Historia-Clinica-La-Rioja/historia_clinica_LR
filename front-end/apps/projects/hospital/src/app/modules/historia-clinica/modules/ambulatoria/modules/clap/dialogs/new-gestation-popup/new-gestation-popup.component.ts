import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA } from '@angular/material/dialog';

@Component({
  selector: 'app-new-gestation-popup',
  templateUrl: './new-gestation-popup.component.html',
  styleUrls: ['./new-gestation-popup.component.scss']
})
export class NewGestationPopupComponent {
	valueGestation:number;
	max=30;
	min=1;
  constructor(@Inject(MAT_DIALOG_DATA) public data) { }

}
