import { Component, Input, OnInit } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { FilterOptionDto, ViolenceReportFilterOptionDto } from '@api-rest/api-model';
import { ViolenceReportFacadeService } from '@api-rest/services/violence-report-facade.service';

@Component({
	selector: 'app-violence-situation-history-filters',
	templateUrl: './violence-situation-history-filters.component.html',
	styleUrls: ['./violence-situation-history-filters.component.scss']
})
export class ViolenceSituationHistoryFiltersComponent implements OnInit {

	@Input() patientId: number;

	form: FormGroup<{
		situationId: FormControl<number>,
		typeId: FormControl<number>,
		modalityId: FormControl<number>,
		institutionId: FormControl<number>
	}>;
	situationNumbers: FilterOptionDto[] = [];
	institutions: FilterOptionDto[] = [];
	types: FilterOptionDto[] = [];
	modalities: FilterOptionDto[] = [];

	constructor(private violenceSituationReportFacadeService: ViolenceReportFacadeService) {}

	ngOnInit(): void {
		this.setForm();
		this.violenceSituationReportFacadeService.setPatientFilters(this.patientId);
		this.violenceSituationReportFacadeService.filters$
			.subscribe((result: ViolenceReportFilterOptionDto) => {
				this.situationNumbers = result.situations;
				this.institutions = result.institutions;
				this.modalities = result.modalities;
				this.types = result.types
			})
	}

	applyFilters() {
		const data: string = JSON.stringify(this.form.value);
		this.violenceSituationReportFacadeService.setEvolutions(this.patientId, data)
	}

	get situationId(): FormControl<number> {
		return this.form.controls.situationId;
	}

	get typeId(): FormControl<number> {
		return this.form.controls.typeId;
	}

	get modalityId(): FormControl<number> {
		return this.form.controls.modalityId;
	}

	get institutionId(): FormControl<number> {
		return this.form.controls.institutionId;
	}

	resetControl(event: Event, control: FormControl) {
		event.stopPropagation();
		control.reset();
	}

	private setForm = () => {
		this.form = new FormGroup({
			situationId: new FormControl(null),
			typeId: new FormControl(null),
			modalityId: new FormControl(null),
			institutionId: new FormControl(null),
		});
	}

}
