
import { Meta, StoryObj } from '@storybook/angular';
import { ShowMoreConceptsComponent } from './show-more-concepts.component';

const meta: Meta<ShowMoreConceptsComponent> = {
	title: 'HSI/ShowMoreConceptsComponent',
	component: ShowMoreConceptsComponent,
	tags: ['autodocs'],
};

export default meta;

type Story = StoryObj<ShowMoreConceptsComponent>;
(args: ShowMoreConceptsComponent) => ({
	props: args,
});

export const singleLongElement: Story = {
	args: {
		concepts: ['ablación con guía de imagen de resonancia magnética largooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo'],
	}
}

export const MultipleElements: Story = {
	args: {
		concepts: ['ablación con guía de imagen de resonancia magnética largooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooo','bilirrubina dosaje total', 'detección de hemoglobina', 'electoforesis de proteínas - medición de HDL'],
	}
}
