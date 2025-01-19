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
    Datagrid,
    EditButton,
    FunctionField,
    Pagination
} from 'react-admin';    

 import CreateRelatedButton from '../../components/CreateRelatedButton';


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

const CreateRule = (props) => {
    const record = useRecordContext(props);
    const customRecord = {institutionalGroupId: record.id};
    return (
        <CreateRelatedButton
            customRecord={customRecord}
            reference="institutionalgrouprules"
            refFieldName="institutionalGroupId"
            label="resources.institutionalgrouprules.createRelated" />
    );
};


const ShowInstitutions = (props) => {
    return  (
        <ReferenceManyField
            id='institutionalgroupinstitutions'
            addLabel={false}
            reference="institutionalgroupinstitutions"
            target="institutionalGroupId"
            pagination={<Pagination />}
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
            pagination={<Pagination />}
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

const ShowLocalRules = (props) => {
    return (
        <ReferenceManyField
            id='institutionalgrouprules'
            addLabel={false}
            reference='institutionalgrouprules'
            target='institutionalGroupId'
            pagination={<Pagination />}
        >
            <Datagrid
                empty={<p style={{marginTop:10, color:'#8c8c8c'}}>Sin reglas asociadas</p>}>
                <TextField label="Especialidad / Práctica o procedimiento" source="ruleName" />
                <TextField label="Nivel" source="ruleLevel" />
                <FunctionField label="Regulación" render={record => record.regulated ? 'Se regula' : 'No se regula'}/>
                <TextField label="Comentario" source="comment" sortable={false}/>
                <EditButton />
            </Datagrid>
        </ReferenceManyField>
    );
};

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
                        <Tab label="Reglas Locales" id="reglaslocales">
                            <CreateRule />
                            <ShowLocalRules />
                        </Tab>
                    </TabbedShowLayout>
                </Fragment>
            </SimpleShowLayout>
        </Show>
    )
};

export default InstitutionalGroupShow;

export {AddInstitutionToGroup, AddUserToGroup, CreateRule, ShowInstitutions, ShowUsers, ShowLocalRules};