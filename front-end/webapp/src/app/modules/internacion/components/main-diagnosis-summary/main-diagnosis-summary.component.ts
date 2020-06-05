import { Component, OnInit, Input } from '@angular/core';
import { DIAGNOSTICO_PRINCIPAL } from '../../constants/summaries';
import { InternmentStateService } from '@api-rest/services/internment-state.service';
import { HealthConditionDto } from '@api-rest/api-model';
import { Observable } from 'rxjs';
import { Router } from '@angular/router';

@Component({
  selector: 'app-main-diagnosis-summary',
  templateUrl: './main-diagnosis-summary.component.html',
  styleUrls: ['./main-diagnosis-summary.component.scss']
})
export class MainDiagnosisSummaryComponent implements OnInit {

	@Input() internmentEpisodeId: number;
	@Input() editable: boolean = true;

	mainDiagnosticosSummary = DIAGNOSTICO_PRINCIPAL;
	mainDiagnosis$: Observable<HealthConditionDto>;

	constructor(
		private internmentStateService: InternmentStateService,
		private router: Router,
	) { }

	ngOnInit(): void {
		this.mainDiagnosis$ = this.internmentStateService.getMainDiagnosis(this.internmentEpisodeId);
	}

	goToClinicalEvaluation(id: number): void {
		this.router.navigate([`${this.router.url}/eval-clinica-diagnosticos/${id}`]);
	}

}
