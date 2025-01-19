import React, {Fragment} from 'react';
import {
    BooleanField,
    Datagrid, DeleteButton,
    ReferenceField,
    ReferenceManyField,
    Show,
    Tab,
    TabbedShowLayout,
    TextField,
    TopToolbar,
    ListButton,
    EditButton
} from 'react-admin';
import {
    SgxDateField,
    CreateRelatedButton,
} from '../../components';
import { ADMINISTRADOR, ADMINISTRADOR_DE_DATOS_PERSONALES, ROOT, ADMINISTRADOR_DE_ACCESO_DOMINIO } from '../../roles';
import { usePermissions } from 'react-admin';

const redirect = (personId) => {
    return `/person/${personId}/show/2`;
};

const UserTab = ({ personId, loaded, total, ...props }) => (
    <Fragment>
        {loaded ? total ? 
                <Datagrid loaded={loaded} total={total} {...props} rowClick="show">
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

const PersonShowActions = ({ data }) => {
    const { permissions } = usePermissions();
    return (!data || !data.id) ? <TopToolbar/> :
        (
            <TopToolbar>
                <ListButton basePath="/person" label="Listar personas"/>
                {permissions?.hasAnyAssignment(ADMINISTRADOR_DE_DATOS_PERSONALES) && <EditButton basePath="/person" record={{ id: data.id }} />}
            </TopToolbar>
        )
};

const PersonShow = props =>{ 
	const { permissions } = usePermissions();
    let personId = props.id;
    return (
    <Show actions={<PersonShowActions />} {...props}>
        <TabbedShowLayout>
            {personalInformationTab()}

			{
            (permissions?.hasAnyAssignment(ROOT, ADMINISTRADOR) && [userTab(personId), professionsTab(personId)]) 
            || 
            (permissions?.hasAnyAssignment(ADMINISTRADOR_DE_ACCESO_DOMINIO) && userTab(personId))
            }
            

        </TabbedShowLayout>
    </Show>
)};

const personalInformationTab = () => {
    return (<Tab label="resources.person.tabs.details" id="personal_information">
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
				<TextField source="email" />
			</Tab>)
}

const userTab = (personId) => {
    return (<Tab label="resources.person.tabs.users" id="users">
				<ReferenceManyField label="resources.person.tabs.users" reference="users" target="personId">
					<UserTab personId={personId} />
				</ReferenceManyField>
			</Tab>)
}

const professionsTab = (personId) => {
    return (<Tab label="resources.professionalprofessions.tab.title" id="professions">
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
			</Tab>)
}

export default PersonShow;
