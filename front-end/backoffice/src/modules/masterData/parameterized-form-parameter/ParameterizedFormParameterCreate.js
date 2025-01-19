import React from 'react';
import {
    Create,
    SimpleForm,
    ReferenceInput,
    AutocompleteInput,
    required
} from 'react-admin';
import CustomToolbar from '../../components/CustomToolbar';
import Typography from '@material-ui/core/Typography';

const redirect = (basePath, id, data) => `/parameterizedform/${data.parameterizedFormId}/show`;

const ParameterizedFormParameterCreate = props => (
    <Create {...props}>
        <SimpleForm toolbar={<CustomToolbar />} redirect={redirect}>
            <div>
                <Typography variant="h5" component="h2" gutterBottom>Agregar parámetro</Typography>
            </div>
            <ReferenceInput
                source="parameterId"
                reference="parameters-autocomplete"
                label="resources.parameterizedform.description"
                filterToQuery={searchText => ({description: searchText})}
                fullWidth
            >
                <AutocompleteInput optionText="description" optionValue="id" resettable={true} validate={[required()]} />
            </ReferenceInput>
        </SimpleForm>
    </Create>
);

export default ParameterizedFormParameterCreate;