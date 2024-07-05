import React from 'react';
import {
    Edit,
    SimpleForm,
    TextInput,
    BooleanInput,
    required,
} from 'react-admin';
import CustomToolbar from '../../components/CustomToolbar';
import Typography from '@material-ui/core/Typography';

const ParameterizedFormEdit = props => (
    <Edit {...props}>
        <SimpleForm toolbar={<CustomToolbar />}>
            <div>
                <Typography variant="h5" component="h2" gutterBottom>Editar Formulario</Typography>
            </div>
            <TextInput source="name" validate={[required()]} />
            <div>
                <Typography variant="h6" component="h3" gutterBottom>Ámbito</Typography>
            </div>
            <BooleanInput label="resources.parameterizedform.outpatient" source="outpatientEnabled" />
            <BooleanInput label="resources.parameterizedform.emergencyCare" source="emergencyCareEnabled" />
            <BooleanInput label="resources.parameterizedform.internment" source="internmentEnabled" />
        </SimpleForm>
    </Edit>
);

export default ParameterizedFormEdit;