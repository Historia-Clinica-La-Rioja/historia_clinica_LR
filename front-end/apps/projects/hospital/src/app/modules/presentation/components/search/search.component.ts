import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { FormBuilder, FormControl, FormGroup } from '@angular/forms';

@Component({
	selector: 'app-search',
	templateUrl: './search.component.html',
	styleUrls: ['./search.component.scss']
})
export class SearchComponent implements OnInit {
	form: FormGroup;
	@Input() label: string = '';
	@Output() descriptionEmmiter: EventEmitter<string> = new EventEmitter<string>();

	constructor(
		readonly formBuilder: FormBuilder,
	) { }

	ngOnInit(): void {
		this.form = this.formBuilder.group({
			description: (FormControl<string>, [null])
		});
	}

	emmit($event: KeyboardEvent ) {
		this.descriptionEmmiter.emit(($event?.target as HTMLInputElement)?.value || '' );
	}

	clearDescription() {
		this.form.controls.description.reset();
		this.descriptionEmmiter.emit();
	}
}
