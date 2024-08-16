import { Component, Input } from '@angular/core';
import { TriageListDto } from '@api-rest/api-model';
import { Triage } from '../triage/triage.component';
import { Observable } from 'rxjs';
import { TriageActionsService } from '../../services/triage-actions.service';


@Component({
	selector: 'app-administrative-triage',
	templateUrl: './administrative-triage.component.html',
	styleUrls: ['./administrative-triage.component.scss']
})
export class AdministrativeTriageComponent {

	@Input() canAssignNotDefinedTriageLevel: boolean;
	@Input() lastTriage$: Observable<TriageListDto>;

	constructor(
		private readonly triageActionsService: TriageActionsService,
	) {	}

	setTriageData(triageData: Triage) {
		this.triageActionsService.triageAdministrative = {
			categoryId: triageData.triageCategoryId,
			doctorsOfficeId: triageData.doctorsOfficeId,
			reasons: triageData.reasons,
			clinicalSpecialtySectorId: triageData.specialtySectorId
		}
	}

}
