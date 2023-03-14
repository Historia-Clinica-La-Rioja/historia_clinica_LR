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

import CustomToolbar from '../components/CustomToolbar';
import PersonReferenceField from '../person/PersonReferenceField';
import Aside from './Aside'
import SgxDateField from "../../dateComponents/sgxDateField";
import SectionTitle from "../components/SectionTitle";
import CreateRelatedButton from "../components/CreateRelatedButton";
import UserRoleSection from "./UserRoleSection";

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
            </SimpleForm>
        </Edit>
    );
}

export default UserEdit;
