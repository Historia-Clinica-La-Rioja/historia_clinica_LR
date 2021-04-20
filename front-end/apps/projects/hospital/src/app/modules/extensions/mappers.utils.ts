import { UIMenuItemDto, UIPageDto } from '@api-rest/api-model';
import { MenuItem} from '@presentation/components/menu/menu.component';
import { Page } from '@presentation/components/page/page.component';

export const mapMenuItem = (item: UIMenuItemDto): MenuItem => {
	const {key, text} = item.label;
	return {label: {key, text}, icon: item.icon, id: item.id, url: `extension/${item.id}`};
}

export const mapMenuItems = (items: UIMenuItemDto[]): MenuItem[] => {
	return items.map(mapMenuItem);
}

export const mapPage = (page: UIPageDto): Page => ({
	type: page.type,
	content: page.content
});
