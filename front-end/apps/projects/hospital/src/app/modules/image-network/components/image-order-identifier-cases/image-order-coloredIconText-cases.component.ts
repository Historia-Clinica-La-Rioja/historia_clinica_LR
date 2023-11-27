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

	@Input()
	set descriptionCases(value: string) {
	};

  constructor() { }

  ngOnInit(): void {
  }

}

export enum IDENTIFIER_IMAGE_CASES {
	WITHOUT_ORDER = 'Orden Pendiente',
	TRANSCRIBED_ORDER = 'Orden Transcripta',
}


