import { Component, Input } from '@angular/core';

@Component({
	selector: 'app-reference-forwarding-label',
	templateUrl: './reference-forwarding-label.component.html',
	styleUrls: ['./reference-forwarding-label.component.scss']
})
export class ReferenceForwardingLabelComponent {

	@Input() forwardingType: string;

}
