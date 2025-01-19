import { EmergencyCareDoctorsOfficeDto, ShockroomDto, EmergencyCareBedDto } from "@api-rest/api-model";
import { SpaceState } from "../components/emergency-care-attention-place-details/emergency-care-attention-place-details.component";

export const getSpaceState = (space: EmergencyCareDoctorsOfficeDto | ShockroomDto | EmergencyCareBedDto): SpaceState => 
    space.isBlocked ? SpaceState.BLOCKED : space.available ? SpaceState.AVAILABLE : SpaceState.NOT_AVAILABLE;
