import React from 'react';
import {
    Edit,
    SimpleForm,
    BooleanInput,
    TextInput,
    ArrayInput,
    DateField,
    SimpleFormIterator,
    ReferenceInput,
    AutocompleteInput,
    SelectInput,
    required,
} from 'react-admin';

import CustomToolbar from '../components/CustomToolbar';
import PersonReferenceField from '../person/PersonReferenceField';
import Aside from './Aside'
import authProvider from '../../providers/authProvider';

const validateInstitutionRequired = (values, entity) => {
    const errors = new Array(values.length);
    values.forEach(function (roleAndInstitution, index) {

        if(roleAndInstitution) {
            let error = {};
            let isAdmin = (authProvider.getRole(roleAndInstitution.roleId) === 'ROOT' ||
                                   authProvider.getRole(roleAndInstitution.roleId) === 'ADMINISTRADOR');

            if(!isAdmin && !roleAndInstitution.institutionId) {
                error.institutionId = 'La institucion es requerida';
            }

            errors[index] = error;
        }
    });

    return errors
};

const UserEdit = props => (
    <Edit {...props} 
        aside={<Aside />} 
    >
        <SimpleForm toolbar={<CustomToolbar />}>
            <PersonReferenceField source="personId" />
            <TextInput source="username" validate={[required()]}/>
            <BooleanInput source="enable" validate={[required()]}/>
            <DateField source="lastLogin" showTime locales="es-AR"/>
            <ArrayInput source="roles" validate={validateInstitutionRequired}>
                <SimpleFormIterator >
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

export default UserEdit;
