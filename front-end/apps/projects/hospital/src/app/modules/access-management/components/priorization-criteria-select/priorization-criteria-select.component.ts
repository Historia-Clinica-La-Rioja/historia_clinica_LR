import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';
import { MasterDataDto } from '@api-rest/api-model';
import { ReferenceMasterDataService } from '@api-rest/services/reference-master-data.service';
import { tap } from 'rxjs';
import { Observable } from 'rxjs/internal/Observable';

@Component({
	selector: 'app-priorization-criteria-select',
	templateUrl: './priorization-criteria-select.component.html',
	styleUrls: ['./priorization-criteria-select.component.scss']
})
export class PriorizationCriteriaSelectComponent implements OnInit {

	form: FormGroup<PriorizationForm>;
	priorities$: Observable<MasterDataDto[]>;

	@Input() oldPriorityId: number;
	@Input() disabled = false;

	@Output() newPriority = new EventEmitter<number>();

	constructor(
		private readonly referenceMasterData: ReferenceMasterDataService,
	) {
		this.createForm();
		this.subscribeToChangesAndEmit();
	}

	ngOnInit(): void {
		this.priorities$ = this.referenceMasterData.getPriorities().pipe(tap(priorities => {
			if (this.oldPriorityId) {
				const priorityToSet = priorities.find(priority => priority.id === this.oldPriorityId);
				this.form.controls.priority.setValue(priorityToSet);
			}
			if (this.disabled) this.form.disable();
		}));
	}

	private createForm() {
		this.form = new FormGroup<PriorizationForm>({
			priority: new FormControl(null, Validators.required),
		});
	}

	private subscribeToChangesAndEmit() {
		this.form.controls.priority.valueChanges.subscribe(priority => this.newPriority.emit(priority.id));
	}

}

interface PriorizationForm {
	priority: FormControl<MasterDataDto>;
}