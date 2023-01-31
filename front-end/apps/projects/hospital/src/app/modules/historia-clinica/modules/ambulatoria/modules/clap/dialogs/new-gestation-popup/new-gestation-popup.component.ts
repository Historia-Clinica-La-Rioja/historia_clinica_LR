import { Component, OnInit } from '@angular/core';

@Component({
  selector: 'app-new-gestation-popup',
  templateUrl: './new-gestation-popup.component.html',
  styleUrls: ['./new-gestation-popup.component.scss']
})
export class NewGestationPopupComponent implements OnInit {
	valueGestation:number;
	max=30;
	min=1;
  constructor() { }

  ngOnInit(): void {
  }

}
