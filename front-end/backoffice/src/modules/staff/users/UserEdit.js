import React from 'react';
import {
    BooleanInput,
    Edit,
    required,
    SimpleForm,
    TextInput,
    ReferenceField,
    EmailField,
    useTranslate
} from 'react-admin';
import {
    SgxDateField,
    CustomToolbar,
    SectionTitle,
    CreateRelatedButton,
} from '../../components';
import PersonReferenceField from '../person/PersonReferenceField';
import Aside from './Aside'
import UserRoleSection from './UserRoleSection';
import HierarchicalUnitSection from './HierarchicalUnitSection';

const redirect = (basePath, id, data) => `/users/${data.id}/show`;

const UserEdit = props => {
    const translate = useTranslate();
    return (
        <Edit {...props} 
            aside={<Aside />} 
        >
            <SimpleForm redirect={redirect} toolbar={<CustomToolbar />}>
                <PersonReferenceField source="personId" />
                <TextInput source="username" validate={[required()]}/>
                <BooleanInput source="enable" validate={[required()]}/>
    
                <ReferenceField source="personId" reference="personextended" label="resources.users.fields.email" link={false}>
                    <EmailField source="email" emptyText={translate('resources.users.noEmail')} />
                </ReferenceField>

                <SgxDateField source="lastLogin" showTime/>
                <SectionTitle label="resources.users.fields.roles"/>
                <CreateRelatedButton
                    reference="userroles"
                    refFieldName="userId"
                    label="resources.users.buttons.linkRole"
                />
                <UserRoleSection/>
                <HierarchicalUnitSection {...props}/>
            </SimpleForm>
        </Edit>
    );
}

export default UserEdit;
