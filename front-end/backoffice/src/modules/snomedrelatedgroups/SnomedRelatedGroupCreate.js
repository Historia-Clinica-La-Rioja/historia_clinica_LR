import React from 'react';
import {
    AutocompleteInput,
    Create,
    ReferenceInput,
    required,
    SelectInput,
    SimpleForm,
} from 'react-admin';
import CustomToolbar from "../components/CustomToolbar";

const redirect = (basePath, id, data) => `/snomedgroups/${data.groupId}/show`;

const SnomedRelatedGroupCreate = props => {
    const parentGroupId = props?.location?.state?.record?.parentGroupId;
    return (
        <Create {...props}>
            <SimpleForm redirect={redirect} toolbar={<CustomToolbar />} >
                <ReferenceInput
                    source="groupId"
                    reference="snomedgroups"
                >
                    <SelectInput optionText="description" optionValue="id" validate={[required()]} options={{ disabled: true }}/>
                </ReferenceInput>

                <ReferenceInput
                    source="snomedId"
                    reference="snomedgroupconcepts"
                    sort={{ field: 'conceptPt', order: 'ASC' }}
                    filterToQuery={searchText => ({ conceptPt: searchText, groupId: parentGroupId })}
                >
                    <AutocompleteInput optionText="conceptPt" optionValue="id" validate={[required()]} resettable />
                </ReferenceInput>

            </SimpleForm>
        </Create>
    );
};

export default SnomedRelatedGroupCreate;
