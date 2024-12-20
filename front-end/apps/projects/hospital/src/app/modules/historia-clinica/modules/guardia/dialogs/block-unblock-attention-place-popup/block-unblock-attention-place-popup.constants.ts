import { BlockUnblockTemplateData, DialogType } from "./block-unblock-attention-place-popup.component";

const TITLE_UNBLOCK = 'guardia.block_unblock_attention_place.dialog.TITLE_UNBLOCK';
const TITLE_BLOCK = 'guardia.block_unblock_attention_place.dialog.TITLE_BLOCK';
const CONFIRM_BLOCK_BUTTON = 'guardia.block_unblock_attention_place.dialog.CONFIRM_BUTTON_BLOCK';
const CONFIRM_UNBLOCK_BUTTON = 'guardia.block_unblock_attention_place.dialog.CONFIRM_BUTTON_UNBLOCK';

export function getTemplateData(type: DialogType): BlockUnblockTemplateData {
	return {
		title: type === 'block' ? TITLE_BLOCK : TITLE_UNBLOCK,
		confirmationButtonDescription: type === 'block' ? CONFIRM_BLOCK_BUTTON : CONFIRM_UNBLOCK_BUTTON,
		confirmationButtonColor: type === 'block' ? 'warn' : 'primary',
		hasCancelButton: type === 'unblock',
		cancelButtonDescription: 'buttons.CANCEL'
	}
}