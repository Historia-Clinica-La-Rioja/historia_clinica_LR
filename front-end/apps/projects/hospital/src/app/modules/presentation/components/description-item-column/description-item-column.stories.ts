import type { Meta, StoryObj } from '@storybook/angular';
import { DescriptionItemColumnComponent } from './description-item-column.component';
import { DateFormat } from '../description-item/description-item.component';


const meta: Meta<DescriptionItemColumnComponent> = {
	title: 'Presentation/DescriptionItemColumnComponent',
	component: DescriptionItemColumnComponent,
	tags: ['autodocs'],
	argTypes: {
		itemsList: {
			description: '<strong>itemsList:</strong> arreglo de tipo DescriptionItemDataInfo que tiene los atributos title, dataId, descriptionData: DescriptionItemData[];',
        },
	},
    parameters: {
        docs: {
            description: {
                component: "Componente para mostrar un listado de title-description-list en forma de grilla"
            }
        }
    },
};

export default meta;

type Story = StoryObj<DescriptionItemColumnComponent>;

export const Classic: Story = {
	args: {
		itemsList: [{
            title: 'Temperatura',
            dataId: 'first-section',
			descriptionData: [{description: '36,3°'}]
		},
        {
            title: 'Frecuencia respiratoria',
            dataId: 'second-section',
			descriptionData: [{description: '22/min'}]
		},
        {
            title: 'Saturación de oxígeno',
            dataId: 'third-section',
			descriptionData: [{description: '87%'}]
		}]
	}
};

export const MoreExamples: Story = {
	args: {
		itemsList: [{
            title: 'Section1',
            dataId: 'first-section',
			descriptionData: [{description: 'value1'}]
		},
        {
            title: 'Section2',
            dataId: 'first-section',
			descriptionData: [{description: 'value2'}]
		},
        {
            title: 'Section3',
            dataId: 'first-section',
			descriptionData: [{description: 'value3'}]
		},
        {
            title: 'Section4',
            dataId: 'first-section',
			descriptionData: [{description: 'value4'}]
		},
        {
            title: 'Section5',
            dataId: 'first-section',
			descriptionData: [{description: 'value5'}]
		},
        {
            title: 'Section6',
            dataId: 'first-section',
			descriptionData: [{description: 'value6'}]
		},
        {
            title: 'Section7',
            dataId: 'first-section',
			descriptionData: [{description: 'value7'}]
		},
        {
            title: 'Section8',
            dataId: 'first-section',
			descriptionData: [{description: 'value8'}]
		},
        {
            title: 'Section9',
            dataId: 'first-section',
			descriptionData: [{description: 'value9'}]
		}]
	}
};

export const ListWithDate: Story = {
	args: {
		itemsList: [{
            title: 'Section1',
            dataId: 'first-section',
			descriptionData: [{
                description: 'value1',
                dateToShow: {
                        date: new Date(),
                        format: DateFormat.DATE_TIME,
                }}]
		},
        {
            title: 'Section2',
            dataId: 'first-section',
			descriptionData: [{
                description: 'value2',
                dateToShow: {
                        date: new Date(),
                        format: DateFormat.DATE_TIME,
                }}]
		},
        {
            title: 'Section3',
            dataId: 'first-section',
			descriptionData: [{
                description: 'value3',
                dateToShow: {
                        date: new Date(),
                        format: DateFormat.DATE_TIME,
                }}]
		},
        {
            title: 'Section4',
            dataId: 'first-section',
			descriptionData: [{
                description: 'value4',
                dateToShow: {
                        date: new Date(),
                        format: DateFormat.DATE_TIME,
                }}]
		},
        {
            title: 'Section5',
            dataId: 'first-section',
			descriptionData: [{
                description: 'value5',
                dateToShow: {
                        date: new Date(),
                        format: DateFormat.DATE_TIME,
                }}]
		},
        {
            title: 'Section6',
            dataId: 'first-section',
			descriptionData: [{
                description: 'value6',
                dateToShow: {
                        date: new Date(),
                        format: DateFormat.DATE_TIME,
                }}]
		},
        {
            title: 'Section7',
            dataId: 'first-section',
			descriptionData: [{
                description: 'value7',
                dateToShow: {
                        date: new Date(),
                        format: DateFormat.DATE_TIME,
                }}]
		},
        {
            title: 'Section8',
            dataId: 'first-section',
			descriptionData: [{
                description: 'value8',
                dateToShow: {
                        date: new Date(),
                        format: DateFormat.DATE_TIME,
                }}]
		},
        {
            title: 'Section9',
            dataId: 'first-section',
			descriptionData: [{
                description: 'value9',
                dateToShow: {
                        date: new Date(),
                        format: DateFormat.DATE_TIME,
                }}]
		}]
	}
};