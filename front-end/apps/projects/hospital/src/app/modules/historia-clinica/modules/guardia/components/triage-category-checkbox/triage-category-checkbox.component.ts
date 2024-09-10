import { Component, forwardRef, OnInit } from '@angular/core';
import { FormControl, FormGroup, NG_VALIDATORS, NG_VALUE_ACCESSOR } from '@angular/forms';
import { AbstractCustomForm } from '@core/abstract-class/AbstractCustomForm';
import { EpisodeFilterService } from '../../services/episode-filter.service';
import { TriageCategory } from '../triage-chip/triage-chip.component';
import { TriageCategoryDto } from '@api-rest/api-model';

const TRIAGE_CATEGORIES = 'triageCategories';

@Component({
	selector: 'app-triage-category-checkbox',
	templateUrl: './triage-category-checkbox.component.html',
	styleUrls: ['./triage-category-checkbox.component.scss'],
	providers: [
		{
			provide: NG_VALUE_ACCESSOR,
			multi: true,
			useExisting: forwardRef(() => TriageCategoryCheckboxComponent),
		},
		{
			provide: NG_VALIDATORS,
			multi: true,
			useExisting: forwardRef(() => TriageCategoryCheckboxComponent),
		},
	],
})
export class TriageCategoryCheckboxComponent extends AbstractCustomForm implements OnInit {

	form: FormGroup;
	formControlsKeys: string[] = [];
	triageCategories: TriageCategoryDto[] = [];

	constructor(
		private readonly episodeFilterService: EpisodeFilterService,
	) {
		super();
	}

	ngOnInit() {
		this.createForm();
		this.episodeFilterService.getCategories().subscribe(triageCategories => {
			this.triageCategories = triageCategories;
			this.addFormControls(triageCategories);
		});
	}

	createForm() {
		this.form = new FormGroup({
			triageCategories: new FormGroup({})
		});
	}

	private addFormControls(triageCategories: TriageCategory[]) {
		const controls = this.initializeControls(triageCategories);
		const form = new FormGroup(controls);
		this.form.setControl(TRIAGE_CATEGORIES, form, { emitEvent: false });
		this.formControlsKeys = Object.keys(this.form.value.triageCategories);
	}

	writeValue(obj: any): void {
		if (!obj)
			this.resetForm();
	}

	private resetForm() {
		const isTheFormInitialized = this.form.value.triageCategories && Object.keys(this.form.value.triageCategories).length;

		if (!!isTheFormInitialized) {
			const controls = Object.keys(this.form.value.triageCategories);
			controls.forEach(control => {
				const triageCategoriesForm = this.form.controls.triageCategories as FormGroup;
				triageCategoriesForm.controls[control].setValue(false, { emitEvent: false });
			});
		}
	}

	private initializeControls(categories: TriageCategory[]) {
		return categories.reduce((control, category) => {
			control[category.id] = new FormControl(null);
			return control;
		}, {});
	}

}
