import React from 'react';
import { Link } from 'react-router-dom';
import Button from '@material-ui/core/Button';
import {
    BooleanField,
    Datagrid,
    DeleteButton,
    EditButton,
    ListButton, 
    Pagination,
    ReferenceField,
    ReferenceManyField,
    Show,
    SimpleShowLayout,
    TextField, TopToolbar,
    usePermissions,
    useTranslate
} from 'react-admin';
import SectionTitle from '../../components/SectionTitle';
import {
    BASIC_BO_ROLES,
} from '../../roles-set';

const CareLineShowActions = ({data, disabled}) => {
    return (!data || !data.id) ? <TopToolbar/> :
        (
            <TopToolbar>
                <ListButton basePath="/carelines" label="Listar Líneas de cuidado"/>
                <EditButton basePath="/carelines" record={{id: data.id}} disabled={disabled}/>
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

const ClassifiedCareLineRoles = ({record, roleCreatePermission}) => {
    if (record.classified) {
        return (
            <div>
                <SectionTitle label="resources.carelinerole.name"/>
                <CreateRelatedButton
                    record={record}
                    reference="carelinerole"
                    refFieldName="careLineId"
                    label="resources.carelinerole.addRelated"
                    disabled={!roleCreatePermission}
                />
                <ReferenceManyField
                    addLabel={false}
                    reference="carelinerole"
                    target="careLineId"
                    sort={{field: 'id', order: 'DESC'}}
                >
                    <Datagrid>
                        <ReferenceField source="roleId" reference="roles" link={false}>
                            <TextField source="description"/>
                        </ReferenceField>
                        <DeleteButton redirect={false} disabled={!roleCreatePermission}/>
                    </Datagrid>
                </ReferenceManyField>
            </div>
        );
    }
    return null;    
}

const CareLineShow = props => {
    const { permissions } = usePermissions();
    const userIsRootOrAdmin = permissions?.hasAnyAssignment(...BASIC_BO_ROLES);
    return(
        <Show {...props} actions={<CareLineShowActions disabled={!userIsRootOrAdmin} />}>
            <SimpleShowLayout>
                <TextField source="description"/>
                <BooleanField source="consultation"/>
                <BooleanField source="procedure"/>
                <BooleanField source="classified"/>

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
                        pagination={<Pagination />}
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

                <ClassifiedCareLineRoles roleCreatePermission={userIsRootOrAdmin}/>

            </SimpleShowLayout>
        </Show>
    );
};

export default CareLineShow;
