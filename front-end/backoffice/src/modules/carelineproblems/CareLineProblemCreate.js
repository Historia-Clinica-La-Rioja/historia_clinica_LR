import React from 'react';
import {
    ReferenceInput,
    AutocompleteInput,
    Create,
    SimpleForm,
    required
} from 'react-admin';
import CustomToolbar from '../components/CustomToolbar';

const redirect = (basePath, id, data) => `/carelines/${data.careLineId}/show`;

const CareLineProblemCreate = props => (
    <Create {...props}>
        <SimpleForm redirect={redirect} toolbar={<CustomToolbar />}>
            <ReferenceInput
                source="careLineId"
                reference="carelines"
                label="resources.carelineproblems.fields.careLineId"
            >
                <AutocompleteInput optionText="description" optionValue="id" options={{ disabled: true }} />
            </ReferenceInput>

            <ReferenceInput
                source='snomedId'
                reference="snomedgroupconcepts"
                label="resources.carelineproblems.fields.snomedId"
                filter={{ groupId: 11}}
                sort= {{field: 'conceptPt', order: 'ASC'}}
                filterToQuery={searchText => ({ conceptPt: searchText})}
            >
                <AutocompleteInput optionText="conceptPt" optionValue="id" validate={[required()]} />
            </ReferenceInput>
        </SimpleForm>
    </Create>
);

export default CareLineProblemCreate;
