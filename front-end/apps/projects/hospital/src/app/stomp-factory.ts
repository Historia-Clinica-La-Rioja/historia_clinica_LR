import { stompConfiguration } from "./stomp.config";
import { StompService } from "./stomp.service";

export function stompServiceFactory() {
	const rxStomp = new StompService();
	rxStomp.configure(stompConfiguration);
	rxStomp.activate();
	return rxStomp;
}
