import { Component, EventEmitter, Input, OnInit, Output } from '@angular/core';
import { TypeaheadOption } from '../typeahead/typeahead.component';
import { TypeaheadV2Service } from '@presentation/services/typeahead-v2.service';

@Component({
	selector: 'app-typeahead-v2',
	templateUrl: './typeahead-v2.component.html',
	styleUrls: ['./typeahead-v2.component.scss'],
	providers: [TypeaheadV2Service]
})
export class TypeaheadV2Component implements OnInit {
	@Input() set options(options: TypeaheadOption<any>[]) {
		this.typeaheadService.setOptions(options);
	};
	@Input() placeholder: string;
	@Input() titleInput: string = ' ';
	@Input() set externalSetValue(externalValue: TypeaheadOption<any>) {
		this.typeaheadService.setExternalSetValue(externalValue);
	};
	@Input() set disabled(disabled: boolean) {
		this.typeaheadService.setDisabled(disabled);
	}

	@Output() selectionChange = new EventEmitter();

	constructor(readonly typeaheadService: TypeaheadV2Service) { }
	ngOnInit() {
		this.typeaheadService.selectValue$.subscribe((option: TypeaheadOption<any>) =>
			this.selectionChange.emit(option?.value)
		)
	}

}
