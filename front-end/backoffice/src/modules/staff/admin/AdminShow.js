import React from 'react';
import {
    Show,
    SimpleShowLayout,
    ReferenceField,
    EmailField,
    useTranslate, TextField, BooleanField
} from 'react-admin';
import {
    SgxDateField,
    CreateRelatedButton,
} from '../../components';
import PersonReferenceField from '../person/PersonReferenceField';
import AdminRoleSection from './AdminRoleSection';

const AdminShow = props => {
    const translate = useTranslate();
    return (
        <Show {...props} hasEdit={false}>
            <SimpleShowLayout>
                <PersonReferenceField source="personId" />
                <TextField source="username" />
                <BooleanField source="enable" />
    
                <ReferenceField source="personId" reference="personextended" label="resources.users.fields.email" link={false}>
                    <EmailField source="email" emptyText={translate('resources.users.noEmail')} />
                </ReferenceField>

                <SgxDateField source="lastLogin" showTime/>
                <CreateRelatedButton
                    reference="userroles"
                    refFieldName="userId"
                    label="resources.users.buttons.linkRole"
                />
                <AdminRoleSection/>
            </SimpleShowLayout>
        </Show>
    );
}

export default AdminShow;
