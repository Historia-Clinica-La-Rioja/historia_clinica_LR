import { Component, EventEmitter, Input, OnDestroy, OnInit, Output, SimpleChanges } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { ViolenceReportDto } from '@api-rest/api-model';
import { ViolenceReportFacadeService } from '@api-rest/services/violence-report-facade.service';
import { Observable, Subscription } from 'rxjs';

@Component({
	selector: 'app-violence-situation-relevant-information-section',
	templateUrl: './violence-situation-relevant-information-section.component.html',
	styleUrls: ['./violence-situation-relevant-information-section.component.scss']
})
export class ViolenceSituationRelevantInformationSectionComponent implements OnInit, OnDestroy {
	@Input() confirmForm: Observable<boolean>;
	@Output() relevantInformation = new EventEmitter<any>();
	violenceSituationSub: Subscription;
	form: FormGroup<{
		observations: FormControl<string>,
	}>;

	constructor(private readonly violenceSituationFacadeService: ViolenceReportFacadeService) { }

	ngOnInit(): void {
		this.setViolenceSituation();
		this.form = new FormGroup({
			observations: new FormControl(null),
		});
	}

	ngOnChanges(changes: SimpleChanges) {
		if(!changes.confirmForm.isFirstChange()){
			this.relevantInformation.emit(this.form.value);
		}
	}

	ngOnDestroy(): void {
		this.violenceSituationSub.unsubscribe();
	}

	private setViolenceSituation() {
		this.violenceSituationSub = this.violenceSituationFacadeService.violenceSituation$
			.subscribe((result: ViolenceReportDto) => this.form.controls.observations.setValue(result.observation));
	}
}
