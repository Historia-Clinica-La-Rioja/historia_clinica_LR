import { CalendarProfessionalViewDockPopupComponent } from '../../dialogs/calendar-professional-view-dock-popup/calendar-professional-view-dock-popup.component';
import { Component, OnDestroy, OnInit } from '@angular/core';
import { DockPopupRef } from '@presentation/services/dock-popup-ref';
import { DockPopupService } from '@presentation/services/dock-popup.service';
import { HealthcareProfessionalService } from '@api-rest/services/healthcare-professional.service';
import { DiariesService } from '@api-rest/services/diaries.service';
import { AppointmentsFacadeService } from '@turnos/services/appointments-facade.service';
import { CalendarProfessionalInformation } from '@turnos/services/calendar-professional-information';

@Component({
	selector: 'app-calendar-professional-view',
	templateUrl: './calendar-professional-view.component.html',
	styleUrls: ['./calendar-professional-view.component.scss']
})
export class CalendarProfessionalViewComponent implements OnInit, OnDestroy {

	dialogRef: DockPopupRef;
	showButton = true;
	hasActiveDiaries = false;

	constructor(
		private readonly dockPopupService: DockPopupService,
		private readonly healthcareProfessional: HealthcareProfessionalService,
		private readonly diaryService: DiariesService,
		private readonly appointmentFacade: AppointmentsFacadeService,
		private readonly calendarProfessionalInfo: CalendarProfessionalInformation
	) { }

	ngOnInit(): void {
		this.healthcareProfessional.getHealthcareProfessionalByUserId().subscribe( professionalId => {
			this.diaryService.hasActiveDiaries(professionalId).subscribe( hasActiveDiary => this.hasActiveDiaries = hasActiveDiary);

		});
	}

	open() {
		this.dialogRef = this.dockPopupService.openOnTop(CalendarProfessionalViewDockPopupComponent);
	}

	ngOnDestroy(): void {
		this.calendarProfessionalInfo.setCalendarDate(new Date());
		this.calendarProfessionalInfo.setProfessionalSelected(null);
	}
}
