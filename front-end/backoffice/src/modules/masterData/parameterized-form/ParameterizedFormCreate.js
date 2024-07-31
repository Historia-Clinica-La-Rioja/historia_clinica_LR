import React from 'react';
import {
    Create,
    BooleanInput,
    SimpleForm,
    TextInput,
    ReferenceInput,
    AutocompleteInput
} from 'react-admin';
import { useLocation } from 'react-router-dom';
import CustomToolbar from '../../components/CustomToolbar';
import Typography from '@material-ui/core/Typography';

const InstitutionInformation = (props) => (
    <ReferenceInput
        source="institutionId"
        reference="institutions"
        sort={{ field: 'name', order: 'ASC' }}
    >
        <AutocompleteInput optionText="name" optionValue="id" options={{ disabled: true }} />
    </ReferenceInput>
)

const ParameterizedFormCreate = props => {
    const location = useLocation();
    const { state } = location;
    const record = state?.record || {};

    const transform = (record) => {
        return {
            name: record.name,
            outpatientEnabled: record.outpatientEnabled,
            emergencyCareEnabled: record.emergencyCareEnabled,
            internmentEnabled: record.internmentEnabled,
            institutionId: record.institutionId
        }
    }
    const validateRequired = (value) => {
        if (!value || value.trim() === '') {
            return 'Requerido';
        }
        return undefined;
    };
    return <Create {...props} transform={transform}>
        <SimpleForm redirect="show" toolbar={<CustomToolbar />}>
            <div>
                <Typography variant="h5" component="h2" gutterBottom>Crear Formulario</Typography>
            </div>
            <TextInput label="resources.parameterizedform.description" source="name" validate={validateRequired} />
            {record.institutionId && <InstitutionInformation />}
            <div>
                <Typography variant="h6" component="h3" gutterBottom>Ámbito</Typography>
            </div>
            <BooleanInput label="resources.parameterizedform.outpatient" source="outpatientEnabled" initialValue={false} />
            <BooleanInput label="resources.parameterizedform.emergencyCare" source="emergencyCareEnabled" initialValue={false} />
            <BooleanInput label="resources.parameterizedform.internment" source="internmentEnabled" initialValue={false} />
        </SimpleForm>
    </Create>
}

export default ParameterizedFormCreate;