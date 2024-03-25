import { type Meta, type StoryObj } from '@storybook/angular';
import { DetailedInformationComponent } from './detailed-information.component';

const meta: Meta<DetailedInformationComponent> = {
	title: 'Presentation/DetailedInformationComponent',
	component: DetailedInformationComponent,
	tags: ['autodocs'],
};

export default meta;

type Story = StoryObj<DetailedInformationComponent>;


export const Full: Story = {
	args: {
		detailedInformation: {
            id: 1,
	        title: "Titulo de Ejemplo",
	        oneColumn: [{
                icon: 'bolt',
	            title: "Titulo de columna 1",
	            value: ["Valor 1", "Valor 2", "Valor 3"],
            }],
	        twoColumns: [{
                icon: 'rocket',
	            title: "Titulo de columna 2",
	            value: ["Valor 1", "Valor 2", "Valor 3"],
            }],
	        threeColumns: [{
                icon: 'bedtime',
	            title: "Titulo de columna 3",
	            value: ["Valor 1", "Valor 2", "Valor 3"],
            }]    
		}
	}
};

export const withoutColumns: Story = {
	args: {
		detailedInformation: {
            id: 1,
	        title: "Titulo de Ejemplo",
		}
	}
};

export const oneColumnWithoutIcon: Story = {
	args: {
		detailedInformation: {
            id: 1,
	        title: "Titulo de Ejemplo",
	        oneColumn: [{
	            title: "Titulo de columna 1",
	            value: ["Valor 1", "Valor 2", "Valor 3"],
            }] 
		}
	}
};

export const OneColumnFull: Story = {
	args: {
		detailedInformation: {
            id: 1,
	        title: "Titulo de Ejemplo",
	        oneColumn: [{
                icon: 'bolt',
	            title: "Titulo de columna 1",
	            value: ["Valor 1", "Valor 2", "Valor 3"],
            }] 
		}
	}
};

export const twoColumnsWithoutIcon: Story = {
	args: {
		detailedInformation: {
            id: 1,
	        title: "Titulo de Ejemplo",
	        twoColumns: [{
	            title: "Titulo de columna 2",
	            value: ["Valor 1", "Valor 2", "Valor 3"],
            }] 
		}
	}
};

export const twoColumnsFull: Story = {
	args: {
		detailedInformation: {
            id: 1,
	        title: "Titulo de Ejemplo",
	        twoColumns: [{
                icon: 'rocket',
	            title: "Titulo de columna 2",
	            value: ["Valor 1", "Valor 2", "Valor 3"],
            }] 
		}
	}
};

export const threeColumnsWithoutIcon: Story = {
	args: {
		detailedInformation: {
            id: 1,
	        title: "Titulo de Ejemplo",
	        threeColumns: [{
	            title: "Titulo de columna 3",
	            value: ["Valor 1", "Valor 2", "Valor 3"],
            }] 
		}
	}
};

export const threeColumnsFull: Story = {
	args: {
		detailedInformation: {
            id: 1,
	        title: "Titulo de Ejemplo",
	        threeColumns: [{
                icon: 'bedtime',
	            title: "Titulo de columna 3",
	            value: ["Valor 1", "Valor 2", "Valor 3"],
            }] 
		}
	}
};
