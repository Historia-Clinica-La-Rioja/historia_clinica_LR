import React from 'react';
import {
    ArrayInput,
    AutocompleteInput,
    Edit,
    ReferenceInput,
    required,
    SelectInput,
    SimpleForm,
    SimpleFormIterator,
    ReferenceField,
    EmailField,
    useTranslate, TextField, BooleanField
} from 'react-admin';

import CustomToolbar from '../components/CustomToolbar';
import PersonReferenceField from '../person/PersonReferenceField';
import authProvider from '../../providers/legacyAuthProvider';
import SgxDateField from "../../dateComponents/sgxDateField";

const redirect = (basePath, id, data) => (data.personId !== -1) ? `/person/${data.personId}/show/1` : '/admin';

const validateInstitutionRequired = (values, entity) => {
    const errors = new Array(values.length);
    values.forEach(function (roleAndInstitution, index) {

        if(roleAndInstitution) {
            let error = {};
            let isAdmin = (authProvider.getRole(roleAndInstitution.roleId) === 'ROOT' ||
                                authProvider.getRole(roleAndInstitution.roleId) === 'ADMINISTRADOR');

            if(!isAdmin && roleAndInstitution.institutionId === -1) {
                error.institutionId = 'La institucion es requerida';
            }

            errors[index] = error;
        }
    });

    return errors
};

const AdminEdit = props => {
    const translate = useTranslate();
    return (
        <Edit {...props} >
            <SimpleForm redirect={redirect} toolbar={<CustomToolbar />}>
                <PersonReferenceField source="personId" />
                <TextField source="username" validate={[required()]}/>
                <BooleanField source="enable" validate={[required()]}/>
    
                <ReferenceField source="personId" reference="personextended" label="resources.users.fields.email" link={false}>
                    <EmailField source="email" emptyText={translate('resources.users.noEmail')} />
                </ReferenceField>

                <SgxDateField source="lastLogin" showTime/>
                <ArrayInput source="roles" validate={validateInstitutionRequired}>
                    <SimpleFormIterator>
                        <ReferenceInput source="roleId" reference="roles" validate={[required()]}>
                            <SelectInput optionText="description" optionValue="id"/>
                        </ReferenceInput>
                        <ReferenceInput
                            source="institutionId"
                            reference="institutions"
                            sort={{ field: 'name', order: 'ASC' }}
                            filterToQuery={searchText => ({name: searchText})}
                        >
                            <AutocompleteInput optionText="name" optionValue="id"/>
                        </ReferenceInput>
                    </SimpleFormIterator>
                </ArrayInput>
            </SimpleForm>
        </Edit>
    );
}

export default AdminEdit;
