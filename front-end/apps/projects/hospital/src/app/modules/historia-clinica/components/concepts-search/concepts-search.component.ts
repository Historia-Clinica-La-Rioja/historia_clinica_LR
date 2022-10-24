import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { TranslateService } from '@ngx-translate/core';
import { FormGroup, FormBuilder, Validators } from '@angular/forms';
import { hasError } from '@core/utils/form.utils';

@Component({
	selector: 'app-concepts-search',
	templateUrl: './concepts-search.component.html',
	styleUrls: ['./concepts-search.component.scss']
})
export class ConceptsSearchComponent implements OnInit {

	@Input() label = '';
	@Input() hideIcon = false;
	@Input() initialSearchValue: string = null;
	@Output() search = new EventEmitter<string>();

	readonly MIN_LENGTH = 3;
	hasError = hasError;
	translatedLabel = '';
	public form: FormGroup;

	constructor(
		private readonly formBuilder: FormBuilder,
		private readonly translateService: TranslateService,
		public dialog: MatDialog,
	) { }

	ngOnInit(): void {
		this.translateService.get(this.label).subscribe(
			translatedText => this.translatedLabel = translatedText.toLowerCase()
		);
		this.form = this.formBuilder.group({
			searchValue: [this.initialSearchValue, [Validators.minLength(this.MIN_LENGTH)]],
		});
		this.emitSearch();
	}

	emitSearch(): void {
		if (this.form.valid) {
			this.search.emit(this.form.controls.searchValue.value);
		}
	}

	clear(): void {
		this.form.controls.searchValue.reset();
	}

}
