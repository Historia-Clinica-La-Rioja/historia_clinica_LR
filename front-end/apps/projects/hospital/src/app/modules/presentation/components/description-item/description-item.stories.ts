import { type Meta, type StoryObj } from '@storybook/angular';

import { DescriptionItemComponent } from './description-item.component';

const meta: Meta<DescriptionItemComponent> = {
	title: 'Presentation/DescriptionItemComponent',
	component: DescriptionItemComponent,
	tags: ['autodocs'],
    argTypes: {
        descriptionData: {
            description: '<strong>descriptionData:</strong> arreglo de tipo DescriptionItemData<br> <strong>description</strong> atributo requerido de tipo string que tiene la descripción a mostrar <br> <strong>dateOrTime</strong> atributo opcional que puede tener un atributo date, time o dateTime (todos tipo Date)',
        },
	},
    parameters: {
        docs: {
            description: {
                component: "Componente para mostrar una descripción o listado de descripciones. Cada descripcion puede estar acompañada por una fecha, hora o fecha y hora debajo. A su vez, si se trata de un listado de descripciones, cada descripcion se mostrara con un bull adelante"
            }
        }
    },
};

export default meta;

type Story = StoryObj<DescriptionItemComponent>;


export const DescriptionWithDateTime: Story = {
	args: {
		descriptionData: [{
			description: 'Propofol 2 gramo/100 ml (20 mg/ml) emulsión inyectable',
			dateOrTime: {
					dateTime: new Date(),
				}
		}]
	}
};

export const DescriptionWithDate: Story = {
	args: {
		descriptionData: [{
			description: 'Propofol 2 gramo/100 ml (20 mg/ml) emulsión inyectable',
			dateOrTime: {
					date: new Date(),
				}
		}]
	}
};

export const DescriptionWithTime: Story = {
	args: {
		descriptionData: [{
			description: 'Propofol 2 gramo/100 ml (20 mg/ml) emulsión inyectable',
			dateOrTime: {
					time: new Date(),
				}
		}]
	}
};

export const DescriptionWithoutDateOrTime: Story = {
	args: {
		descriptionData: [{
			description: 'Propofol 2 gramo/100 ml (20 mg/ml) emulsión inyectable',
		}]
	}
};

export const MultipleDescriptionWithDateTime: Story = {
	args: {
		descriptionData: [{
			description: 'Propofol 2 gramo/100 ml (20 mg/ml) emulsión inyectable',
			dateOrTime: {
					dateTime: new Date(),
				}
		},
        {
			description: 'Propionato de clobetasol 500 mcg/g, ungüento cutáneo',
			dateOrTime: {
					dateTime: new Date(),
				}
		},
        {
			description: 'Propofol 500 mg/50 ml (10 mg/ml) emulsión inyectable',
			dateOrTime: {
					dateTime: new Date(),
				}
		},
        ]
	}
};

export const MultipleDescriptionWithDate: Story = {
	args: {
		descriptionData: [{
			description: 'Propofol 2 gramo/100 ml (20 mg/ml) emulsión inyectable',
			dateOrTime: {
					date: new Date(),
				}
		},
        {
			description: 'Propionato de clobetasol 500 mcg/g, ungüento cutáneo',
			dateOrTime: {
					date: new Date(),
				}
		},
        {
			description: 'Propofol 500 mg/50 ml (10 mg/ml) emulsión inyectable',
			dateOrTime: {
					date: new Date(),
				}
		},
        ]
	}
};

export const MultipleDescriptionWithTime: Story = {
	args: {
		descriptionData: [{
			description: 'Propofol 2 gramo/100 ml (20 mg/ml) emulsión inyectable',
			dateOrTime: {
					time: new Date(),
				}
		},
        {
			description: 'Propionato de clobetasol 500 mcg/g, ungüento cutáneo',
			dateOrTime: {
					time: new Date(),
				}
		},
        {
			description: 'Propofol 500 mg/50 ml (10 mg/ml) emulsión inyectable',
			dateOrTime: {
					time: new Date(),
				}
		},
        ]
	}
};

export const MultipleDescriptionWithoutDateOrTime: Story = {
	args: {
		descriptionData: [{
			description: 'Propofol 2 gramo/100 ml (20 mg/ml) emulsión inyectable',
		},
        {
            description: 'Propionato de clobetasol 500 mcg/g, ungüento cutáneo',
        },
        {
            description: 'Propofol 500 mg/50 ml (10 mg/ml) emulsión inyectable',
        }]
	}
};



export const MultipleDescriptionWithoutMixedAttributes: Story = {
	args: {
		descriptionData: [{
			description: 'Propofol 2 gramo/100 ml (20 mg/ml) emulsión inyectable',
			dateOrTime: {
					date: new Date(),
				}
		},
        {
            description: 'Propofol 500 mg/50 ml (10 mg/ml) emulsión inyectable',
        },
        {
			description: 'Propofol 2 gramo/100 ml (20 mg/ml) emulsión inyectable',
			dateOrTime: {
					dateTime: new Date(),
				}
		},
        {
			description: 'Propionato de clobetasol 500 mcg/g, ungüento cutáneo',
			dateOrTime: {
					time: new Date(),
				}
		},
        {
            description: 'Propofol 500 mg/50 ml (10 mg/ml) emulsión inyectable',
        },
        {
            description: 'Propofol 500 mg/50 ml (10 mg/ml) emulsión inyectable',
        }]
	}
};