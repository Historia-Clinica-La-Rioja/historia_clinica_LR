import { AfterViewInit, Component, ElementRef, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';

@Component({
	selector: 'app-tooth',
	templateUrl: './tooth.component.html',
	styleUrls: ['./tooth.component.scss']
})
export class ToothComponent implements OnInit, AfterViewInit {

	constructor() { }

	@ViewChild('svg') view: ElementRef<HTMLInputElement>;
	arrayTeethParts: any;

	@Input() toothId?;
	@Input() toothTreatment: ToothTreatment = ToothTreatment.AS_FRACTIONAL_TOOTH;

	@Output() mouseOverEmitter = new EventEmitter();
	@Output() mouseClickEmitter = new EventEmitter();

	ngOnInit() {
	}

	ngAfterViewInit(): void {

		this.arrayTeethParts = Array.from(this.view.nativeElement.children.item(0).childNodes);

		if (this.toothTreatment === ToothTreatment.AS_FRACTIONAL_TOOTH) {
		} else {
			this.view.nativeElement.addEventListener('mouseover', () => this.view.nativeElement.style.backgroundColor = '#F8F8F8');
			this.view.nativeElement.addEventListener('mouseout', () => this.view.nativeElement.style.backgroundColor = 'transparent');
			this.view.nativeElement.addEventListener('click', () => this.mouseClickEmitter.emit(`${this.toothId}`));
		}
	}

}

export enum ToothTreatment {
	AS_WHOLE_TOOTH, AS_FRACTIONAL_TOOTH
}
