import { Component, Input, OnInit, Output } from '@angular/core';
import { HCEHospitalizationHistoryDto } from '@api-rest/api-model';
import { HceGeneralStateService } from '@api-rest/services/hce-general-state.service';
import { DateFormat, momentFormat, momentParseDate } from '@core/utils/moment.utils';
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
					problema.entryDate = momentFormat(momentParseDate(problema.entryDate), DateFormat.VIEW_DATE);
					problema.dischargeDate = problema.dischargeDate ? momentFormat(momentParseDate(problema.dischargeDate), DateFormat.VIEW_DATE) : undefined;
					return problema;
				});
			})
		);
	}

	goToEmergencyCareEpisode(episodeId: number) {
		this.goToEpisode.next(episodeId);
	}

}
