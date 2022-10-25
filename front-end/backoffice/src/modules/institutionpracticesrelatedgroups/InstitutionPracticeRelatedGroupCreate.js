import React from 'react';
import {
    AutocompleteInput,
    Create,
    ReferenceInput,
    required,
    SelectInput,
    SimpleForm, usePermissions,
} from 'react-admin';
import CustomToolbar from "../components/CustomToolbar";
import { ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE } from "../roles";

const redirect = (basePath, id, data) => `/institutionpractices/${data.groupId}/show`;

const InstitutionPracticeRelatedGroupCreate = props => {
    const { permissions } = usePermissions();
    const userIsAdminInstitutional = permissions?.roleAssignments?.filter(roleAssignment => (roleAssignment.role === ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE.role)).length > 0;
    return (
        <Create {...props} hasCreate={userIsAdminInstitutional}>
            <SimpleForm redirect={redirect} toolbar={<CustomToolbar />} >
                <ReferenceInput
                    source="groupId"
                    reference="snomedgroups"
                >
                    <SelectInput optionText="description" optionValue="id" validate={[required()]} options={{ disabled: true }}/>
                </ReferenceInput>

                <ReferenceInput
                    source="snomedId"
                    reference="snowstormpractices"
                    filterToQuery={searchText => ({conceptPt: searchText})}
                >
                    <AutocompleteInput optionText="conceptPt" optionValue="id" validate={[required()]} resettable helperText="* Mínimo 3 caracteres para realizar la búsqueda en Snowstorm" />
                </ReferenceInput>

            </SimpleForm>
        </Create>
    );
};

export default InstitutionPracticeRelatedGroupCreate;
