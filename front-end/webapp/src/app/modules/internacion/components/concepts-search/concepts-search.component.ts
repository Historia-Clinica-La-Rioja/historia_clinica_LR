import { Component, OnInit, Output, EventEmitter, Input, ViewChild, ElementRef } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { ConceptsSearchDialogComponent } from '../../dialogs/concepts-search-dialog/concepts-search-dialog.component';
import { SnomedDto } from '@api-rest/api-model';
import { AnamnesisFormService } from '../../services/anamnesis-form.service';
import { Subscription } from 'rxjs';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
	selector: 'app-concepts-search',
	templateUrl: './concepts-search.component.html',
	styleUrls: ['./concepts-search.component.scss']
})
export class ConceptsSearchComponent implements OnInit {

	private subscription: Subscription;

	@Input() text: string = '';
	@Output() onSelect = new EventEmitter<SnomedDto>();
	@ViewChild('buttonSubmit') buttonSubmit;
	selectedConcept: SnomedDto;
	form: FormGroup;

	constructor(
		public dialog: MatDialog,
		private formBuilder: FormBuilder,
		private anamnesisFormService: AnamnesisFormService
	) {
		this.subscription = anamnesisFormService.submitted.subscribe(
			submitted => {
				if (submitted) {
					const buttonElement: HTMLElement = this.buttonSubmit._elementRef.nativeElement as HTMLElement;
					buttonElement.click();
					this.anamnesisFormService.changeSubmitted(false);
				}
			}
		);
	}

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			search: [null]
		});
	}

	openDialog(): void {
		if (!this.form.controls.search.value) return;
		const dialogRef = this.dialog.open(ConceptsSearchDialogComponent, {
			width: '70%',
			data: { searchValue: this.form.controls.search.value }
		});

		dialogRef.afterClosed().subscribe(
			(selectedConcept: SnomedDto) => {
				this.onSelect.emit(selectedConcept);
				this.form.controls.search.setValue(selectedConcept.fsn);
				this.selectedConcept = selectedConcept;
			}
		);
	}

	submit() {
		if (!this.selectedConcept) {
			this.form.controls.search.setErrors({required: true});
		}
	}

	clear(): void {
		this.onSelect.emit();
		this.form.controls.search.setValue('');
		this.selectedConcept = undefined;
	}

}
