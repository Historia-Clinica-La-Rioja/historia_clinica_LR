import { Component, Inject, OnInit } from '@angular/core';
import { OVERLAY_DATA } from '@presentation/presentation-model';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';

@Component({
  selector: 'app-surgical-report-dock-popup',
  templateUrl: './surgical-report-dock-popup.component.html',
  styleUrls: ['./surgical-report-dock-popup.component.scss']
})
export class SurgicalReportDockPopupComponent implements OnInit {

	constructor(
		@Inject(OVERLAY_DATA) public data: any,
		public dockPopupRef: DockPopupRef,
	) { }

  ngOnInit(): void {
  }

}
