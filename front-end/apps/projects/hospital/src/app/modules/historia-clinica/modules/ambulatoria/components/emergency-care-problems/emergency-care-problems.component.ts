import { Component, Input, OnInit, Output } from '@angular/core';
import { HCEHospitalizationHistoryDto } from '@api-rest/api-model';
import { dateTimeDtotoLocalDate, dateToDateTimeDto } from '@api-rest/mapper/date-dto.mapper';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
import { SummaryHeader } from '@presentation/components/summary-card/summary-card.component';
import { Observable, Subject, map } from 'rxjs';

@Component({
	selector: 'app-emergency-care-problems',
	templateUrl: './emergency-care-problems.component.html',
	styleUrls: ['./emergency-care-problems.component.scss']
})
export class EmergencyCareProblemsComponent implements OnInit {

	@Input() patientId: number;
	@Output() goToEpisode = new Subject<number>();

	readonly header: SummaryHeader = {
		matIcon: 'check',
		title: 'Problemas de Guardia'
	};

	emergencyCareProblems$: Observable<HCEHospitalizationHistoryDto[]>;

	constructor(
		private readonly hceGeneralStateService: HceGeneralStateService,
	) { }

	ngOnInit(): void {
		this.emergencyCareProblems$ = this.hceGeneralStateService.getEmergencyCareHistory(this.patientId).pipe(
			map((problemas: HCEHospitalizationHistoryDto[]) => {
				return problemas.map((problema: HCEHospitalizationHistoryDto) => {
					problema.entryDate = dateToDateTimeDto(dateTimeDtotoLocalDate(problema.entryDate));
					problema.dischargeDate = problema.dischargeDate ? dateToDateTimeDto(dateTimeDtotoLocalDate(problema.dischargeDate)) : undefined;
					return problema;
				});
			})
		);
	}

	goToEmergencyCareEpisode(episodeId: number) {
		this.goToEpisode.next(episodeId);
	}

}
