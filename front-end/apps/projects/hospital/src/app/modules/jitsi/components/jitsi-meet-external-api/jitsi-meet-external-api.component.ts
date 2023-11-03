import { Component, Input, OnInit } from '@angular/core';
import { JitsiService } from 'projects/hospital/src/app/modules/jitsi/jitsi.service';

@Component({
	selector: 'app-jitsi-meet-external-api',
	templateUrl: './jitsi-meet-external-api.component.html',
	styleUrls: ['./jitsi-meet-external-api.component.scss'],
})
export class JitsiMeetExternalAPIComponent implements OnInit {

	@Input() roomId: string;
	@Input() userName: string

	constructor(public jitsiService: JitsiService) { }

	ngOnInit(): void {
		this.jitsiService.moveRoom(this.roomId, this.userName);
	}

	/* executeCommand(data) {
		console.log(
			'this.jitsiService.getParticipants():',
			this.jitsiService.getParticipants()
		);

		this.jitsiService.api.executeCommand(
			'sendEndpointTextMessage',
			this.jitsiService.getParticipants(),
			'mover a principal'
		);
	} */
}
