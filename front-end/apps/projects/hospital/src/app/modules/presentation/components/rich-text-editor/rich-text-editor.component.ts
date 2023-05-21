import { Component, Input } from '@angular/core';
import { FormControl, FormGroup } from '@angular/forms';

@Component({
	selector: 'app-rich-text-editor',
	templateUrl: './rich-text-editor.component.html',
	styleUrls: ['./rich-text-editor.component.scss']
})
export class RichTextEditorComponent {

	@Input() placeholder = "";
	@Input() formParent: FormGroup;
	@Input() controlParent: FormControl;
	@Input() config = {
		toolbar: [
			['bold', 'italic', 'underline', 'strike'],
			['blockquote'],
			[{ list: 'ordered' }, { list: 'bullet' }],
			[{ header: [1, 2, 3, 4, 5, 6, false] }],
			['link'],
		],
	}

	constructor() { }

}
