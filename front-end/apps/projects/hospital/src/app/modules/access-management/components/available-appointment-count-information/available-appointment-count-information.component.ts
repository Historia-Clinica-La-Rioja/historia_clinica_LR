import { Component, Input } from '@angular/core';
import { DiaryAvailableAppointmentsSearchService } from '@turnos/services/diary-available-appointments-search.service';
import { Observable } from 'rxjs/internal/Observable';
import { DestinationInstitutionInformation } from '../destination-institution-information/destination-institution-information.component';

@Component({
	selector: 'app-available-appointment-count-information',
	templateUrl: './available-appointment-count-information.component.html',
	styleUrls: ['./available-appointment-count-information.component.scss']
})
export class AvailableAppointmentCountInformationComponent {

	private _destinationInstitutionInfo: DestinationInstitutionInformation;
	availableAppointmentCount$: Observable<number>;
	availableProtectedAppointmentCount$: Observable<number>;

	@Input()
	set destinationInstitutionInformation(destinationInstitutionInfo: DestinationInstitutionInformation) {
		this._destinationInstitutionInfo = destinationInstitutionInfo;
		if (destinationInstitutionInfo.referenceInstitution.id)
			this.loadAvailableAppointmentsCount();
	}

	constructor(
		private readonly diaryAvailableAppointmentsSearchService: DiaryAvailableAppointmentsSearchService,
	) { }

	private loadAvailableAppointmentsCount() {
		const institutionId = this._destinationInstitutionInfo.referenceInstitution.id;
		const departmentId = this._destinationInstitutionInfo.referenceInstitution.departmentId;

		const careLineId = this._destinationInstitutionInfo.careLineId;
		const clinicalSpecialtiesIds = this._destinationInstitutionInfo.clinicalSpecialtiesIds;
		const practiceId = this._destinationInstitutionInfo.practiceId;
		
		if (careLineId) {
			this.availableProtectedAppointmentCount$ = this.diaryAvailableAppointmentsSearchService.getAvailableProtectedAppointmentsQuantity(institutionId, clinicalSpecialtiesIds, departmentId, careLineId, practiceId);
			this.availableAppointmentCount$ = this.diaryAvailableAppointmentsSearchService.getAvailableAppiuntmentsQuantityByCarelineDiaries(institutionId, careLineId, practiceId, clinicalSpecialtiesIds);
		}
		else
			this.availableAppointmentCount$ = this.diaryAvailableAppointmentsSearchService.getAvailableAppointmentsQuantity(institutionId, clinicalSpecialtiesIds, practiceId);
	}
}
