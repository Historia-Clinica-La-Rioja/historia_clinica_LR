import { environment } from "@environments/environment";
import { RxStompConfig } from "@stomp/rx-stomp";

const deployedLocally = window.location.hostname.startsWith('localhost') || window.location.hostname.startsWith('127.0.0.1');

export const stompConfiguration: RxStompConfig = {
	brokerURL: deployedLocally || !environment.production ? `ws://localhost:8080${environment.apiBase}/ws` : `wss://${window.location.hostname}${environment.apiBase}/ws`,
}
