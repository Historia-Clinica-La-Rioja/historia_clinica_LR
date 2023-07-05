import React from 'react';
import {
    Show,
    SimpleShowLayout,
    ReferenceField,
    EmailField,
    useTranslate, usePermissions
} from 'react-admin';

import PersonReferenceField from '../person/PersonReferenceField';
import SgxDateField from "../../dateComponents/sgxDateField";
import SectionTitle from "../components/SectionTitle";
import CreateRelatedButton from "../components/CreateRelatedButton";
import UserRoleSection from "./UserRoleSection";
import {TextField} from "react-admin";
import {BooleanField} from "react-admin";
import HierarchicalUnitSection from "./HierarchicalUnitSection";
import {UNIDADES_JEARQUICAS_FF} from "../institutions/InstitutionShow";


const UserShow = props => {
    const translate = useTranslate();
    const { permissions } = usePermissions();
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
                { permissions && permissions.isOn(UNIDADES_JEARQUICAS_FF) && <HierarchicalUnitSection {...props}/>}
            </SimpleShowLayout>
        </Show>
    );
}

export default UserShow;
