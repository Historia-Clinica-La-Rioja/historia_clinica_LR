import { Component, Input } from '@angular/core';
import { TranslateService } from '@ngx-translate/core';
import { Color } from '@presentation/colored-label/colored-label.component';
import { ColoredIconText } from '@presentation/components/colored-icon-text/colored-icon-text.component';
import { PresentationModule } from '@presentation/presentation.module';

const ADMINISTRATIVE_CLOSURE = 'turnos.search_references.ADMINISTRATIVE_CLOSURE';

@Component({
	selector: 'app-reference-state-label',
	templateUrl: './reference-state-label.component.html',
	styleUrls: ['./reference-state-label.component.scss'],
	standalone: true,
	imports: [PresentationModule]
})
export class ReferenceStateLabelComponent {

	referenceState = PENDING_CLOSURE;

	@Input() set referenceClosureDescription(referenceClosureDescription: string) {
		if (referenceClosureDescription)
			this.referenceState = {
				...CLOSURE,
				color: referenceClosureDescription === this.translateService.instant(ADMINISTRATIVE_CLOSURE)? Color.GREY : CLOSURE.color,
				text: referenceClosureDescription
			}
	}

	constructor(private readonly translateService: TranslateService) { }

}

const PENDING_CLOSURE: ColoredIconText = {
	icon: 'swap_horiz',
	color: Color.YELLOW,
	text: 'turnos.search_references.REFERENCE_REQUESTED'
}

const CLOSURE: ColoredIconText = {
	icon: 'swap_horiz',
	color: Color.GREEN,
	text: ""
}
