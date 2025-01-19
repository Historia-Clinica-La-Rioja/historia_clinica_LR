import { moduleMetadata, type Meta, type StoryObj } from '@storybook/angular';
import { ListElementItemComponent } from './list-element-item.component';
import { EIndicationType } from '@api-rest/api-model';
import { PresentationModule } from '@presentation/presentation.module';



const meta: Meta<ListElementItemComponent> = {
	title: 'HSI/ListElementItemComponent',
	component: ListElementItemComponent,
	tags: ['autodocs'],
	decorators: [
		moduleMetadata({
			imports: [PresentationModule]
		}),
	],
};

export default meta;

type Story = StoryObj<ListElementItemComponent>;


export const full: Story = {
	args: {
		content: {
			createdBy: 'Joaquin Lardapide',
			description: 'Hacer mas componentes de presentación',
			status: {
				cssClass: 'red',
				description: 'Pendiente',
				type: EIndicationType.DIET
			},
			timeElapsed: '12/12/12 - 12:12',
			extra_info: [
				{
					title: 'extra_info_title',
					content: 'extra_info_content'
				}
			],
			id: 1,
			observations: 'Observaciones extras'
		},
		indication: true
	},
};

export const withoutExtraInfo: Story = {
	args: {
		content: {
			createdBy: 'Someone',
			description: 'Description',
			status: {
				cssClass: 'blue',
				description: 'Status',
				type: EIndicationType.DIET
			},
			timeElapsed: '12/12/2022',
			id: 1,
			observations: 'observation'
		},
		indication: true
	},
};

export const withoutObservations: Story = {
	args: {
		content: {
			createdBy: 'Someone',
			description: 'Description',
			status: {
				cssClass: 'blue',
				description: 'Status',
				type: EIndicationType.DIET
			},
			timeElapsed: '12/12/2022',
			id: 1,
		},
		indication: true
	},
};

export const withoutBoolean: Story = {
	args: {
		content: {
			createdBy: 'Someone',
			description: 'Description',
			status: {
				cssClass: 'grey',
				description: 'Status',
				type: EIndicationType.DIET
			},
			timeElapsed: '12/12/2022',
			id: 1,
		},
	},
};
export const nullObservations: Story = {
	args: {
		content: {
			createdBy: 'Someone',
			description: 'Description',
			status: {
				cssClass: 'grey',
				description: 'Status',
				type: EIndicationType.DIET
			},
			timeElapsed: '12/12/2022',
			id: 1,
		},
		indication: true
	},
};


