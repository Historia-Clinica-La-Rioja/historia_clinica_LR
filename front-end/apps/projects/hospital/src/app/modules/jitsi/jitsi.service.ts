import { Injectable } from '@angular/core';
import { JitsiCallService } from 'projects/hospital/src/app/modules/jitsi/jitsi-call.service';
declare var JitsiMeetExternalAPI: any;

@Injectable({
	providedIn: 'root',
})
export class JitsiService {
	api: any;
	options: any;
	domain: string = "localhost:8443";

	// For Custom Controls
	isAudioMuted = true;
	isVideoMuted = true;

	constructor(
		/* private route: Router, */
		private readonly jitsiCallService: JitsiCallService
	) { }

	moveRoom(nameRoom: string, userName: string): void {
		this.options = {
			roomName: nameRoom,
			interfaceConfigOverwrite: {
				startAudioMuted: true,
				startVideoMuted: true,
				prejoinPageEnabled: false
			},
			parentNode: document.querySelector('#jitsi-iframe'),
			userInfo: {
				displayName: userName
			},
			configOverwrite: {
				prejoinPageEnabled: false
			}
		};

		this.api = new JitsiMeetExternalAPI(this.domain, this.options);
		this.api.addEventListeners({
			readyToClose: this.handleClose,
			participantLeft: this.handleParticipantLeft,
			participantJoined: this.handleParticipantJoined,
			videoConferenceJoined: this.handleVideoConferenceJoined,
			videoConferenceLeft: this.handleVideoConferenceLeft,
			audioMuteStatusChanged: this.handleMuteStatus,
			videoMuteStatusChanged: this.handleVideoStatus,
			participantRoleChanged: this.participantRoleChanged,
			passwordRequired: this.passwordRequired,
			endpointTextMessageReceived: this.endpointTextMessageReceived,
		});
	}
	handleClose = () => {
		console.log('handleClose');
		this.jitsiCallService.close();
	};

	endpointTextMessageReceived = async (event) => {
	};

	passwordRequired = async () => {
	};

	handleParticipantLeft = async (participant: any) => {
	};

	participantRoleChanged = async (participant: any) => {
	};

	handleParticipantJoined = async (participant: any) => {
	};

	handleVideoConferenceJoined = async (participant: any) => {
		console.log('handleVideoConferenceJoined', participant); // { roomName: "bwb-bfqi-vmh", id: "8c35a951", displayName: "Akash Verma", formattedDisplayName: "Akash Verma (me)"}
	};

	handleVideoConferenceLeft = () => {
		this.jitsiCallService.close();
	};

	handleMuteStatus = (audio: any) => {
		console.log('handleMuteStatus', audio); // { muted: true }
	};

	handleVideoStatus = (video: any) => {
		console.log('handleVideoStatus', video); // { muted: true }
	};

	getParticipants() {
		return new Promise((resolve, reject) => {
			setTimeout(() => {
				resolve(this.api.getParticipantsInfo()); // get all participants
			}, 500);
		});
	}

	// custom events
	/* executeCommand(command: string) {
		this.api.executeCommand(command);
		if (command == 'hangup') {
			this.route.navigate(['/thank-you']);
			return;
		}

		if (command == 'toggleAudio') {
			this.isAudioMuted = !this.isAudioMuted;
		}

		if (command == 'toggleVideo') {
			this.isVideoMuted = !this.isVideoMuted;
		}
	} */
}
