import React, {Fragment} from 'react';
import {
    BooleanField,
    Datagrid, DeleteButton,
    ReferenceField,
    ReferenceManyField,
    Show,
    Tab,
    TabbedShowLayout,
    TextField
} from 'react-admin';
import CreateRelatedButton from '../components/CreateRelatedButton';
import SgxDateField from "../../dateComponents/sgxDateField";

const redirect = (personId) => {
    return `/person/${personId}/show/2`;
};

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
            <Tab label="resources.person.tabs.details" id="personal_information">
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

            <Tab label="resources.person.tabs.users" id="users">
                <ReferenceManyField label="resources.person.tabs.users" reference="users" target="personId">
                    <UserTab personId={personId} />
                </ReferenceManyField>
            </Tab>
            
            <Tab label="resources.professionalprofessions.tab.title" id="professions">
                <ReferenceManyField addLabel={false} reference="professionalprofessions" target="personId">
                    <Datagrid rowClick="show">
                        <ReferenceField source="professionalSpecialtyId" reference="professionalspecialties" link={false}>
                            <TextField source="description" />
                        </ReferenceField>
                        <DeleteButton redirect={redirect(personId)}/>
                    </Datagrid>
                </ReferenceManyField>
                <CreateRelatedButton
                customRecord={{personId: personId}}
                reference="professionalprofessions"
                refFieldName="personId"
                label="resources.person.buttons.linkProfession"/>
            </Tab>

        </TabbedShowLayout>
    </Show>
)};

export default PersonShow;
