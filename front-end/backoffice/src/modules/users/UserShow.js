import React from 'react';
import {
    Show,
    SimpleShowLayout,
    ReferenceField,
    EmailField,
    useTranslate
} from 'react-admin';

import PersonReferenceField from '../person/PersonReferenceField';
import SgxDateField from "../../dateComponents/sgxDateField";
import SectionTitle from "../components/SectionTitle";
import CreateRelatedButton from "../components/CreateRelatedButton";
import UserRoleSection from "./UserRoleSection";
import {TextField} from "react-admin";
import {BooleanField} from "react-admin";
import HierarchicalUnitSection from "./HierarchicalUnitSection";

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
            </SimpleShowLayout>
        </Show>
    );
}

export default UserShow;
