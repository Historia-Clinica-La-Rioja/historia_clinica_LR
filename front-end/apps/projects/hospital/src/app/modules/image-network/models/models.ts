import { StudyAppointmentDto } from "@api-rest/api-model";
import { State } from "../components/worklist/worklist.component";

export interface StudyAppointment {
	info: StudyAppointmentDto;
	state: State;
	imageSize: String;
}
