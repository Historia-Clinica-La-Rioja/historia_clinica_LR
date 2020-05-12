import { Component, OnInit, Input } from '@angular/core';
import { DIAGNOSTICO_PRINCIPAL } from '../../constants/summaries';
import { InternmentStateService } from '@api-rest/services/internment-state.service';
import { HealthConditionDto } from '@api-rest/api-model';
import { Observable } from 'rxjs';

@Component({
  selector: 'app-main-diagnosis-summary',
  templateUrl: './main-diagnosis-summary.component.html',
  styleUrls: ['./main-diagnosis-summary.component.scss']
})
export class MainDiagnosisSummaryComponent implements OnInit {

	@Input() internmentEpisodeId: number;

	mainDiagnosticosSummary = DIAGNOSTICO_PRINCIPAL;
	mainDiagnosis$: Observable<HealthConditionDto>;

	constructor(
		private internmentStateService: InternmentStateService,
	) { }

	ngOnInit(): void {
		this.mainDiagnosis$ = this.internmentStateService.getMainDiagnosis(this.internmentEpisodeId);
	}

}
