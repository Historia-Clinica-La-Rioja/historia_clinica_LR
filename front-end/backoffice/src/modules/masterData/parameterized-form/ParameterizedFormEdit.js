import React from 'react';
import {
    Edit,
    SimpleForm,
    TextInput,
    BooleanInput,
    ReferenceInput,
    AutocompleteInput,
    useRecordContext
} from 'react-admin';
import CustomToolbar from '../../components/CustomToolbar';
import Typography from '@material-ui/core/Typography';

const InstitutionInformation = props => (
    <ReferenceInput
        source="institutionId"
        reference="institutions"
        sort={{ field: 'name', order: 'ASC' }}
    >
        <AutocompleteInput optionText="name" optionValue="id" options={{ disabled: true }} />
    </ReferenceInput>
);

const ParameterizedFormEdit = props => {
    const record = useRecordContext(props);
    const transform = (record) => {
        return {
            id: record.id,
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
    return (
        <Edit {...props} transform={transform}>
            <SimpleForm redirect="show" toolbar={<CustomToolbar />}>
                <div>
                    <Typography variant="h5" component="h2" gutterBottom>Editar Formulario</Typography>
                </div>
                <TextInput source="name" validate={validateRequired} />
                {record?.institutionId && <InstitutionInformation />}
                <div>
                    <Typography variant="h6" component="h3" gutterBottom>Ámbito</Typography>
                </div>
                <BooleanInput label="resources.parameterizedform.outpatient" source="outpatientEnabled" />
                <BooleanInput label="resources.parameterizedform.emergencyCare" source="emergencyCareEnabled" />
                <BooleanInput label="resources.parameterizedform.internment" source="internmentEnabled" />
            </SimpleForm>
        </Edit>
    )
};

export default ParameterizedFormEdit;