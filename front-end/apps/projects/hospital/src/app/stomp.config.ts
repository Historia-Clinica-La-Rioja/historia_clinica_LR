import { RxStompConfig } from "@stomp/rx-stomp";

export const stompConfiguration: RxStompConfig = {
	brokerURL: 'ws://localhost:8080/api/ws',

	debug: (msg: string): void => {
		console.log(new Date(), msg);
	},
}
