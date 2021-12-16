import { UIMenuItemDto } from './extensions-model';
import { MenuItem} from '@presentation/components/menu/menu.component';

export const mapMenuItem = (item: UIMenuItemDto): MenuItem => {
	const {key, text} = item.label;
	return {label: {key, text}, icon: item.icon, id: item.id, url: `extension/${item.id}`};
}

export const mapMenuItems = (items: UIMenuItemDto[]): MenuItem[] => {
	return items.map(mapMenuItem);
}
