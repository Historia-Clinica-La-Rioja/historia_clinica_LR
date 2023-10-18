import React, { Fragment } from 'react';
import { 
    SimpleShowLayout,
    Show,
    TextField,
    TabbedShowLayout,
    Tab,
    useRecordContext,
    ReferenceField,
    DeleteButton,
    ReferenceManyField,
    Datagrid
} from "react-admin";    

 import CreateRelatedButton from "../components/CreateRelatedButton";


const AddInstitutionToGroup = (props) => {
    const record = useRecordContext(props);
    const customRecord = {institutionalGroupId: record.id};
    return (
        <CreateRelatedButton
            customRecord={customRecord}
            reference="institutionalgroupinstitutions"
            refFieldName="institutionalGroupId"
            label="resources.institutionalgroupinstitutions.createRelated"/>
    );
};

const AddUserToGroup = (props) => {
    const record = useRecordContext(props);
    const customRecord = {institutionalGroupId: record.id};
    return (
        <CreateRelatedButton
            customRecord={customRecord}
            reference="institutionalgroupusers"
            refFieldName="institutionalGroupId"
            label="resources.institutionalgroupusers.createRelated" />
    );
};

const ShowInstitutions = (props) => {
    return  (
        <ReferenceManyField
            id='institutionalgroupinstitutions'
            addLabel={false}
            reference="institutionalgroupinstitutions"
            target="institutionalGroupId"
        >
        <Datagrid
                  empty={<p style={{marginTop:10, color:'#8c8c8c'}}>Sin instituciones asociadas</p>}>
            <TextField label='Institución' source="institutionName" />
            <TextField label='Partido' source="departmentName"/>
            <DeleteButton redirect={false} />
        </Datagrid>
    </ReferenceManyField>
    );
};

const ShowUsers = (props) => {
    return (
        <ReferenceManyField
            id='institutionalgroupusers'
            addLabel={false}
            reference='institutionalgroupusers'
            target='institutionalGroupId'
        >
            <Datagrid
                empty={<p style={{marginTop:10, color:'#8c8c8c'}}>Sin usuarios asociadas</p>}>
                <ReferenceField source="userId" reference="manageruserpersons" label='Nombre' link={false}>
                    <TextField label='Nombre' source='completeName' />
                </ReferenceField>
                <ReferenceField source="userId" reference="manageruserpersons" label="Rol" link={false}>
                    <TextField label='Rol' source='role' />
                </ReferenceField>
                <DeleteButton redirect={false} />
            </Datagrid>
        </ReferenceManyField>
    )
}

const InstitutionalGroupShow = (props) => {
    return (
        <Show {...props}>
            <SimpleShowLayout>
                <ReferenceField source="typeId" reference="institutionalgrouptypes" link={false}>
                  <TextField source="value" />
                </ReferenceField>
                <TextField source="name" />
                <Fragment>
                    <TabbedShowLayout>
                        <Tab label="Instituciones" id="instituciones">
                            <AddInstitutionToGroup/>
                            <ShowInstitutions />
                        </Tab>
                        <Tab label="Usuarios" id="usuarios">
                            <AddUserToGroup />
                            <ShowUsers />
                        </Tab>
                    </TabbedShowLayout>
                </Fragment>
            </SimpleShowLayout>
        </Show>
    )
};

export default InstitutionalGroupShow;

export {AddInstitutionToGroup, AddUserToGroup, ShowInstitutions, ShowUsers};