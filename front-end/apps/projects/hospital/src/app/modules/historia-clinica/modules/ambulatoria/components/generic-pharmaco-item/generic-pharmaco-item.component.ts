import { AfterViewInit, Component, ElementRef, Input, ViewChild } from '@angular/core';
import { SnomedMedicationSearchDto } from '@api-rest/api-model';

@Component({
	selector: 'app-generic-pharmaco-item',
	templateUrl: './generic-pharmaco-item.component.html',
	styleUrls: ['./generic-pharmaco-item.component.scss']
})
export class GenericPharmacoItemComponent implements AfterViewInit {

	isTextEllipsed = false;
	@Input() pharmaco: SnomedMedicationSearchDto;
	@ViewChild('textContentElement', { static: false }) textContentElement: ElementRef;

	constructor() { }

	ngAfterViewInit() {
		this.checkIfTextEllipsed();
	}

	checkIfTextEllipsed() {
		const element = this.textContentElement.nativeElement;
		this.isTextEllipsed = element.scrollWidth > element.clientWidth;
	}

}
