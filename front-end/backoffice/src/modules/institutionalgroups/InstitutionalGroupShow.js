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
                        </Tab>
                    </TabbedShowLayout>
                </Fragment>
            </SimpleShowLayout>
        </Show>
    )
};

export default InstitutionalGroupShow;

export {AddInstitutionToGroup, ShowInstitutions};