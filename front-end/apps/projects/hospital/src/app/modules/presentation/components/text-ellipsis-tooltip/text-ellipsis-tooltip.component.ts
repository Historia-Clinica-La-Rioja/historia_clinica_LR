import { AfterViewInit, Component, ElementRef, Input, ViewChild } from '@angular/core';

@Component({
	selector: 'app-text-ellipsis-tooltip',
	templateUrl: './text-ellipsis-tooltip.component.html',
	styleUrls: ['./text-ellipsis-tooltip.component.scss']
})
export class TextEllipsisTooltipComponent implements AfterViewInit {

	isTextEllipsed = false;
	@Input() text: string;
	@ViewChild('textContentElement', { static: false }) textContentElement: ElementRef;

	constructor() { }

	ngAfterViewInit() {
		setTimeout(() => {
			this.checkIfTextEllipsed();
		});
	}

	checkIfTextEllipsed() {
		const element = this.textContentElement.nativeElement;
		this.isTextEllipsed = element.scrollWidth > element.clientWidth;
	}

}
