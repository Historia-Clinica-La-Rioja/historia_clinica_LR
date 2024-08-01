import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { Subscription } from 'rxjs';

@Component({
	selector: 'app-option-list-parameter',
	templateUrl: './option-list-parameter.component.html',
	styleUrls: ['./option-list-parameter.component.scss']
})
export class OptionListParameterComponent implements OnInit, OnDestroy {

	private formSubscription: Subscription;
	form: FormGroup;
	@Input() options: OptionListParameter[];
	@Input() title: string;
	@Output() optionSelected = new EventEmitter<number>();

	constructor() { }

	ngOnInit(): void {
		this.form = new FormGroup({
			optionId: new FormControl<number>(null)
		});

		this.formSubscription = this.form.valueChanges.subscribe(valueChanges => this.optionSelected.emit(valueChanges.optionId));
	}

	ngOnDestroy(): void {
		this.formSubscription.unsubscribe();
	}

}

export interface OptionListParameter {
	id: number;
	description: string;
}
