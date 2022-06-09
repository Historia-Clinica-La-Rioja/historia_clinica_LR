import { CalendarProfessionalViewDockPopupComponent } from '../../dialogs/calendar-professional-view-dock-popup/calendar-professional-view-dock-popup.component';
import { Component, OnInit } from '@angular/core';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { DockPopupService } from '@presentation/services/dock-popup.service';
import { HealthcareProfessionalService } from '@api-rest/services/healthcare-professional.service';
import { DiariesService } from '@api-rest/services/diaries.service';

@Component({
	selector: 'app-calendar-professional-view',
	templateUrl: './calendar-professional-view.component.html',
	styleUrls: ['./calendar-professional-view.component.scss']
})
export class CalendarProfessionalViewComponent implements OnInit {

	dialogRef: DockPopupRef;
	showButton = true;
	hasActiveDiaries = false;

	constructor(
		private readonly dockPopupService: DockPopupService,
		private readonly healthcareProfessional: HealthcareProfessionalService,
		private readonly diaryService: DiariesService
	) { }

	ngOnInit(): void {
		this.healthcareProfessional.getHealthcareProfessionalByUserId().subscribe( professionalId => {
			this.diaryService.hasActiveDiaries(professionalId).subscribe( hasActiveDiary => this.hasActiveDiaries = hasActiveDiary);
			
		});
	}

	open() {
		this.dialogRef = this.dockPopupService.openOnTop(CalendarProfessionalViewDockPopupComponent);
	}
}
