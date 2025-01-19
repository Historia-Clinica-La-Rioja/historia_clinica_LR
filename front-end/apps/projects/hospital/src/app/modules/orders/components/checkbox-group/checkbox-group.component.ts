import { Component, EventEmitter, Input, Output } from '@angular/core';

@Component({
	selector: 'app-checkbox-group',
	templateUrl: './checkbox-group.component.html',
	styleUrls: ['./checkbox-group.component.scss']
})
export class CheckboxGroupComponent {
	@Input() title: string = '';
	@Input() options: { id: number; name: string; checked?: boolean }[] = [];
	@Output() selectionChange = new EventEmitter<number[]>();

	emitSelectionChange() {
		const selectedIds = this.options.filter(option => option.checked).map(option => option.id);
		this.selectionChange.emit(selectedIds);
	}
}
