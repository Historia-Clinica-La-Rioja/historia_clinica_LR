import React from 'react';
import {
    Show,
    SimpleShowLayout,
    ReferenceField,
    EmailField,
    useTranslate,
    TextField,
    BooleanField,
} from 'react-admin';
import {
    SgxDateField,
    SectionTitle,
    CreateRelatedButton,
} from '../../components';
import PersonReferenceField from '../person/PersonReferenceField';
import UserRoleSection from './UserRoleSection';
import HierarchicalUnitSection from './HierarchicalUnitSection';
import InstitutionalGroupSection from './InstitutionalGroupSection';

const UserShow = props => {
    const translate = useTranslate();
    return (
        <Show {...props}>
            <SimpleShowLayout>
                <PersonReferenceField source="personId" />
                <TextField source="username"/>
                <BooleanField source="enable"/>
    
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
               <InstitutionalGroupSection />
            </SimpleShowLayout>
        </Show>
    );
}

export default UserShow;
