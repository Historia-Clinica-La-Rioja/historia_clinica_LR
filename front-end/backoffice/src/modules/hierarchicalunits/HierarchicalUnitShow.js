import React, {Fragment} from 'react';
import {
    Show,
    TextField,
    ReferenceField,
    SimpleShowLayout,
    Labeled,
    useRecordContext,
    usePermissions,
    ReferenceManyField,
    Datagrid,
    DeleteButton
} from 'react-admin';

import {ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE} from "../roles";
import SectionTitle from "../components/SectionTitle";
import CreateRelatedButton from '../components/CreateRelatedButton';

const SERVICE = 8;

const ServiceField = (props) => {
    const record = useRecordContext(props);
    return record.typeId !== SERVICE ? null : (
        <Labeled label="resources.hierarchicalunits.fields.clinicalSpecialtyId">
            <ReferenceField link={false} reference="clinicalservices" source="clinicalSpecialtyId">
                <TextField source="name"/>
            </ReferenceField>
        </Labeled>
    )
}

const HierarchicalUnitParents = (props) => {
    const record = useRecordContext(props);
    const show = (basePath, id, data) => `/hierarchicalunits/${data.hierarchicalUnitParentId}/show`;
    return record ?
        (
            <Fragment>
                <SectionTitle label="resources.hierarchicalunitrelationships.parents.name"/>
                <AsociateHierarchicalUnitParent {...props}/>
                <ReferenceManyField
                    addLabel={false}
                    reference="hierarchicalunitrelationships"
                    target="hierarchicalUnitChildId"
                >
                    <Datagrid rowClick={show}
                              empty={<p style={{paddingLeft:10, marginTop:0, color:'#8c8c8c'}}>Sin unidades jerárquicas padres definidas</p>}>
                        <ReferenceField source="hierarchicalUnitParentId" label="resources.hierarchicalunits.fields.alias" reference="hierarchicalunits" link={false}>
                            <TextField source="alias"/>
                        </ReferenceField>
                        <DeleteButton redirect={false} disabled={!UserIsInstitutionalAdmin()}/>
                    </Datagrid>
                </ReferenceManyField>
            </Fragment>
        ) : null;
}

const UserIsInstitutionalAdmin = function () {
    const { permissions } = usePermissions();
    const userAdmin= permissions?.roleAssignments?.filter(roleAssignment => (roleAssignment.role === ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE.role)).length > 0;
    return userAdmin;
}

const AsociateHierarchicalUnitParent = ({ record }) => {
    const customRecord = {hierarchicalUnitChildId: record.id};
    return UserIsInstitutionalAdmin() ?( <CreateRelatedButton
            customRecord={customRecord}
            reference="hierarchicalunitrelationships"
            refFieldName="hierarchicalUnitChildId"
            label="resources.hierarchicalunitrelationships.parents.addRelated"
            />
    ) : null;
};

const HierarchicalUnitShow = props => (
    <Show {...props} hasEdit={UserIsInstitutionalAdmin()}>
        <SimpleShowLayout>
            <ReferenceField source="institutionId" reference="institutions">
                <TextField source="name"/>
            </ReferenceField>
            <TextField source="alias"/>
            <ReferenceField source="typeId" reference="hierarchicalunittypes" link={ false }>
                <TextField source="description" />
            </ReferenceField>
            <ServiceField {...props} />
            <HierarchicalUnitChilds/>
            <HierarchicalUnitParents/>
        </SimpleShowLayout>
    </Show>
);

export default HierarchicalUnitShow;

export { HierarchicalUnitParents }