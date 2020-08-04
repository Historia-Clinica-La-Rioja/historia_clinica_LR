import { Component, OnInit, Input } from '@angular/core';

@Component({
  selector: 'app-chip-text',
  templateUrl: './chip-text.component.html',
  styleUrls: ['./chip-text.component.scss']
})
export class ChipTextComponent implements OnInit {

	@Input('text') text: string;
	@Input('error') error: boolean;

	constructor(
	) { }

	ngOnInit() {
	}

}
