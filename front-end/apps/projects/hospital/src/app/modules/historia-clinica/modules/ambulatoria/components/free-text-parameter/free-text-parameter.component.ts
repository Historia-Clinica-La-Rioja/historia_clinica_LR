import { Component, EventEmitter, Input, OnDestroy, OnInit, Output } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';
import { Subscription } from 'rxjs';

@Component({
	selector: 'app-free-text-parameter',
	templateUrl: './free-text-parameter.component.html',
	styleUrls: ['./free-text-parameter.component.scss']
})
export class FreeTextParameterComponent implements OnInit, OnDestroy {

	private formSubscription: Subscription;
	form: FormGroup<FreeTextParameterForm>;

	@Input() title: string;
	@Output() freeText = new EventEmitter<string>();

	constructor() { }

	ngOnInit(): void {
		this.form = new FormGroup<FreeTextParameterForm>({
			freeText: new FormControl<string>(null)
		});

		this.formSubscription = this.form.valueChanges.subscribe(valueChanges =>
			this.freeText.emit(valueChanges.freeText)
		);
	}

	ngOnDestroy(): void {
		this.formSubscription.unsubscribe();
	}

}

export interface FreeTextParameterForm {
	freeText: FormControl<string>;
}