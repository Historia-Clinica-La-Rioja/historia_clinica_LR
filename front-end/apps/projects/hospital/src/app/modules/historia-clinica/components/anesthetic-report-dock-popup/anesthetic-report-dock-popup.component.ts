import { Component, OnInit } from '@angular/core';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';

@Component({
    selector: 'app-anesthetic-report-dock-popup',
    templateUrl: './anesthetic-report-dock-popup.component.html',
    styleUrls: ['./anesthetic-report-dock-popup.component.scss']
})
export class AnestheticReportDockPopupComponent implements OnInit {

	isLoading = false;

    constructor(
		public dockPopupRef: DockPopupRef) { }

    ngOnInit(): void {
    }

    save(): void {

	}
}
