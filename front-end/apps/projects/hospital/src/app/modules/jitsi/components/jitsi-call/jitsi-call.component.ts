import { Component, Input, OnInit, ViewChild, ViewContainerRef } from '@angular/core';
import { JitsiCallService } from 'projects/hospital/src/app/modules/jitsi/jitsi-call.service';
import { NewJitsiCallComponent } from '../new-jitsi-call/new-jitsi-call.component';

@Component({
	selector: 'app-jitsi-call',
	templateUrl: './jitsi-call.component.html',
	styleUrls: ['./jitsi-call.component.scss']
})
export class JitsiCallComponent implements OnInit {

	@Input() userName: string;
	@ViewChild('jitsiCall', { read: ViewContainerRef }) jitsiCall: ViewContainerRef;

	constructor(
		private readonly jitsiCallService: JitsiCallService,

	) { }

	ngOnInit(): void {

		this.jitsiCallService.$roomId.subscribe(
			roomId => {
				if (roomId) {
					const ref = this.jitsiCall.createComponent(NewJitsiCallComponent);
					ref.setInput('roomId', roomId);
					ref.setInput('userName', this.jitsiCallService.userName)
				} else {
					this.jitsiCall.clear();
				}

			}
		)
	}

}
