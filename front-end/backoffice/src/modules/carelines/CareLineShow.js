import React from 'react';
import {
    BooleanField,
    Datagrid,
    DeleteButton,
    EditButton,
    ListButton,
    ReferenceField,
    ReferenceManyField,
    Show,
    SimpleShowLayout,
    TextField, TopToolbar,
    usePermissions,
    useTranslate
} from 'react-admin';
import SectionTitle from '../components/SectionTitle';
import Button from "@material-ui/core/Button";
import { Link } from 'react-router-dom';
import {ADMINISTRADOR, ROOT} from "../roles";

const CareLineShowActions = ({data}) => {
    const { permissions } = usePermissions();
    const userIsRootOrAdmin = permissions?.roleAssignments?.filter(roleAssignment => (roleAssignment.role === ADMINISTRADOR.role) || (roleAssignment.role === ROOT.role)).length > 0;
    return (!data || !data.id) ? <TopToolbar/> :
        (
            <TopToolbar>
                <ListButton basePath="/carelines" label="Listar Líneas de cuidado"/>
                <EditButton basePath="/carelines" record={{id: data.id}} disabled={!userIsRootOrAdmin}/>
            </TopToolbar>
        )
};

const CreateRelatedButton = ({record, reference, refFieldName, label, disabled}) => {
    const newRecord = record ? {[refFieldName]: record.id} : null;
    const translate = useTranslate();
    return (
        <Button
            component={Link}
            to={{
                pathname: `/${reference}/create`,
                state: { record: newRecord },
            }}
            disabled={disabled}
        >{translate(label)}</Button>
    );
};

const CareLineShow = props => {
    const { permissions } = usePermissions();
    const userIsRootOrAdmin = permissions?.roleAssignments?.filter(roleAssignment => (roleAssignment.role === ADMINISTRADOR.role) || (roleAssignment.role === ROOT.role)).length > 0;
    return(
        <Show {...props} actions={<CareLineShowActions/>}>
            <SimpleShowLayout>
                <TextField source="description"/>
                <BooleanField source="consultation"/>
                <BooleanField source="procedure"/>
                <SectionTitle label="resources.clinicalspecialtycarelines.name"/>
                <CreateRelatedButton
                    reference="clinicalspecialtycarelines"
                    refFieldName="careLineId"
                    label="resources.clinicalspecialtycarelines.addRelated"
                    disabled={!userIsRootOrAdmin}
                />
                <ReferenceManyField
                    addLabel={false}
                    reference="clinicalspecialtycarelines"
                    target="careLineId"
                    sort={{field: 'id', order: 'DESC'}}
                >
                    <Datagrid>
                        <ReferenceField source="clinicalSpecialtyId" reference="clinicalspecialties">
                            <TextField source="name"/>
                        </ReferenceField>
                        <DeleteButton redirect={false} disabled={!userIsRootOrAdmin}/>
                    </Datagrid>
                </ReferenceManyField>
                <SectionTitle label="resources.carelineproblems.name"/>
                <CreateRelatedButton
                    reference="carelineproblems"
                    refFieldName="careLineId"
                    label="resources.carelineproblems.addRelated"
                    disabled={!userIsRootOrAdmin}
                />
                <ReferenceManyField
                    addLabel={false}
                    reference="carelineproblems"
                    target="careLineId"
                    sort={{field: 'id', order: 'DESC'}}
                >
                    <Datagrid>
                        <ReferenceField source="snomedId" reference="snomedconcepts" link="show">
                            <TextField source="pt"/>
                        </ReferenceField>
                        <DeleteButton redirect={false} disabled={!userIsRootOrAdmin} />
                    </Datagrid>
                </ReferenceManyField>
            </SimpleShowLayout>
        </Show>
    );
};

export default CareLineShow;
