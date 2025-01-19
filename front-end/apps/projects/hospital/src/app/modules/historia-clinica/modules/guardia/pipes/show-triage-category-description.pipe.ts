import { Pipe, PipeTransform } from '@angular/core';
import { TriageCategoryDto } from '@api-rest/api-model';

@Pipe({
	name: 'showTriageCategoryDescription'
})
export class ShowTriageCategoryDescriptionPipe implements PipeTransform {

	transform(categoryId: string, categories: TriageCategoryDto[]): string {
		if (!categoryId || !categories.length)
			return "";
		const category = categories.find(category => category.id === Number(categoryId));
		return !!category ? `${category.name} - ${category.color.name}` : "";
	}

}
