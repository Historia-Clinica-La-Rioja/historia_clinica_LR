import { Component, Input } from '@angular/core';
import { JitsiCallService } from 'projects/hospital/src/app/modules/jitsi/jitsi-call.service';

@Component({
	selector: 'app-new-jitsi-call',
	templateUrl: './new-jitsi-call.component.html',
	styleUrls: ['./new-jitsi-call.component.scss']
})
export class NewJitsiCallComponent {

	@Input() roomId: string;
	@Input() userName: string;

	constructor(
		private readonly jitsiCallService: JitsiCallService
	) { }

	close() {
		this.jitsiCallService.close();
	}

}
