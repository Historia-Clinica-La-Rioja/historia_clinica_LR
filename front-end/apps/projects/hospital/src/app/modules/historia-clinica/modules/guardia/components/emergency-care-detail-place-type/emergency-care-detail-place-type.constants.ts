import { Color } from "@presentation/colored-label/colored-label.component"
import { AvailableButtonType } from "@presentation/components/button-availability/button-availability.component"
import { SpaceState } from "../emergency-care-attention-place-details/emergency-care-attention-place-details.component"

const AVAILABLE_SPACE = {
	labelDescription: 'guardia.home.attention_places.space.FREE',
	labelColor: Color.GREEN,
	buttonType: AvailableButtonType.AVAILABLE
}

const NOT_AVAILABLE_SPACE = {
	labelDescription: 'guardia.home.attention_places.space.NOT_FREE',
	labelColor: Color.RED,
	buttonType: AvailableButtonType.NOT_AVAILABLE
}

const BLOCKED_SPACE = {
	labelDescription: 'guardia.home.attention_places.space.BLOCKED',
	labelColor: Color.RED,
	buttonType: AvailableButtonType.BLOCKED
}

export const PLACE_TYPE = {
	[SpaceState.AVAILABLE]: AVAILABLE_SPACE,
	[SpaceState.NOT_AVAILABLE]: NOT_AVAILABLE_SPACE,
	[SpaceState.BLOCKED]: BLOCKED_SPACE,
}
