import type { Meta, StoryObj } from '@storybook/angular';
import { ItemSummaryComponent, Size } from './item-summary.component';



const meta: Meta<ItemSummaryComponent> = {
	title: 'Presentation/ItemSummaryComponent',
	component: ItemSummaryComponent,
	tags: ['autodocs'],
	argTypes: {
		size: {
			options: Object.values(Size),
			control: {
				type: 'select',
			},
			description: 'Usar el enum Size'
		}
	}
};

export default meta;
type Story = StoryObj<ItemSummaryComponent>;

export const small_complete: Story = {
	args: {
		data: {
			title: 'Titulo',
			subtitle: 'Primer Subtitulo',
			subtitle2: 'Segundo subtitulo',
			avatar: '/assets/icons/advice.svg'
		},
		size: Size.SMALL
	}
};


export const small_title_avatar: Story = {
	args: {
		data: { title: 'Joaquin Lardapide', avatar: 'assets/images/error_outline.png'},
		size: Size.SMALL
	}
};

export const small_subtitle_noAvatar: Story = {
	args: {
		data: {
			title: 'Joaquin Lardapide',
			subtitle: 'DESARROLLADOR',
		},
		size: Size.SMALL
	}
};

export const small_subtitle2_noAvatar: Story = {
	args: {
		data: {
			title: 'Joaquin Lardapide',
			subtitle2: 'Boca Juniors'
		},

		size: Size.SMALL
	}

};

export const big_complete: Story = {
	args: {
		data: {
			title: 'Laboratorio',
			subtitle: 'Muestra',
			subtitle2: '27/03/96',
			avatar: '/assets/icons/laboratory.svg'
		},

		size: Size.SMALL
	}
};

export const big_title_avatar: Story = {
	args: {
		data: { title: 'HSI', avatar:'/assets/custom/icons/icon-152x152.png'},
		size: Size.BIG
	}
};

