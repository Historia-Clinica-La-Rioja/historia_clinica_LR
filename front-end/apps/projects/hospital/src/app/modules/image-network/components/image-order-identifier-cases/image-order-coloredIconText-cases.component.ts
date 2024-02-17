import { Component, Input, OnInit } from '@angular/core';
import { Color } from '@presentation/colored-label/colored-label.component';
import { ColoredIconText } from '@presentation/components/colored-icon-text/colored-icon-text.component';

@Component({
	selector: 'app-image-order-coloredIconText-cases',
	templateUrl: './image-order-coloredIconText-cases.component.html',
	styleUrls: ['./image-order-coloredIconText-cases.component.scss']
})
export class ImageOrderColoredIconTextCasesComponent implements OnInit {

	Color = Color
	without_order: ColoredIconText = {
		icon: 'error_outlined',
		text: 'Orden Pendiente',
		color: Color.RED
	}

	trancribed_order: ColoredIconText = {
		icon: 'attachment_outline',
		text: 'Orden Transcripta',
		color: Color.BLUE
	}

	currentTypeOrder: ColoredIconText

	@Input()
	set descriptionCases(value: IDENTIFIER_IMAGE_CASES) {
		this.currentTypeOrder = value === IDENTIFIER_IMAGE_CASES.TRANSCRIBED_ORDER ? this.trancribed_order : this.without_order
	};

	constructor() { }

	ngOnInit(): void {
	}

}

export enum IDENTIFIER_IMAGE_CASES {
	WITHOUT_ORDER = 'Orden Pendiente',
	TRANSCRIBED_ORDER = 'Orden Transcripta',
}


