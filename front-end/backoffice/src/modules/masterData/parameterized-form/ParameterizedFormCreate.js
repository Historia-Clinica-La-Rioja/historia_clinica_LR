import React from 'react';
import {
    Create,
    required,
    BooleanInput,
    SimpleForm,
    TextInput,
} from 'react-admin';
import CustomToolbar from '../../components/CustomToolbar';
import Typography from '@material-ui/core/Typography';


const ParameterizedFormCreate = props => (
    <Create {...props}>
        <SimpleForm redirect="show" toolbar={<CustomToolbar />}>
            <div>
                <Typography variant="h5" component="h2" gutterBottom>Crear Formulario</Typography>
            </div>
            <TextInput label="resources.parameterizedform.description" source="name" validate={[required()]} />
            <div>
                <Typography variant="h6" component="h3" gutterBottom>Ámbito</Typography>
            </div>
            <BooleanInput label="resources.parameterizedform.outpatient" source="outpatientEnabled" initialValue={false} />
            <BooleanInput label="resources.parameterizedform.emergencyCare" source="emergencyCareEnabled" initialValue={false} />
            <BooleanInput label="resources.parameterizedform.internment" source="internmentEnabled" initialValue={false} />
        </SimpleForm>
    </Create>
);

export default ParameterizedFormCreate;