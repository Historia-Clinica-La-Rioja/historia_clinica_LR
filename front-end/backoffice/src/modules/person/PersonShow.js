import React, {Fragment} from 'react';
import {
    BooleanField,
    Datagrid,
    ReferenceField,
    ReferenceManyField,
    Show,
    Tab,
    TabbedShowLayout,
    TextField
} from 'react-admin';
import CreateRelatedButton from '../components/CreateRelatedButton';
import HealthcareProfessionalSpecialtiesTab from './HealthcareProfessionalSpecialitiesTab';
import SgxDateField from "../../dateComponents/sgxDateField";

const UserTab = ({ personId, loaded, total, ...props }) => (
    <Fragment>
        {loaded ? total ? 
                <Datagrid loaded={loaded} total={total} {...props} rowClick="edit">
                    <TextField source="username" />
                    <BooleanField source="enable" />
                    <SgxDateField source="lastLogin" />
                </Datagrid>
            : <CreateRelatedButton
                customRecord={{personId: personId}}
                reference="users"
                refFieldName="personId"
                label="resources.users.createRelated"/> 
            : null
        }
    </Fragment>
);


const PersonShow = props =>{ 
    let personId = props.id;
    return (
    <Show {...props}>
        <TabbedShowLayout>
            <Tab label="resources.person.tabs.details">
                <TextField source="firstName" />
                <TextField source="middleNames" />
                <TextField source="lastName" />
                <TextField source="otherLastNames" />
                <ReferenceField source="identificationTypeId" reference="identificationTypes" link={false}>
                    <TextField source="description" />
                </ReferenceField>
                <TextField source="identificationNumber" />
                <ReferenceField source="genderId" reference="genders" link={false}>
                    <TextField source="description" />
                </ReferenceField>
                <SgxDateField source="birthDate" />
            </Tab>

            <Tab label="resources.person.tabs.users">
                <ReferenceManyField label="resources.person.tabs.users" reference="users" target="personId">
                    <UserTab personId={personId} />
                </ReferenceManyField>
            </Tab>
            
            <Tab label="resources.healthcareprofessionalspecialties.name">
                <HealthcareProfessionalSpecialtiesTab />
            </Tab>

        </TabbedShowLayout>
    </Show>
)};

export default PersonShow;
