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
    DeleteButton,
    FunctionField,
    BooleanField, 
    EditButton
} from 'react-admin';

import {ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE} from '../../roles';
import SectionTitle from '../../components/SectionTitle';
import CreateRelatedButton from '../../components/CreateRelatedButton';

const SERVICE = 8;

const renderPersonData = (institutionUserPerson) => `${institutionUserPerson.completeName ?institutionUserPerson.completeName : "" } ${institutionUserPerson.completeLastName ?institutionUserPerson.completeLastName : "" } `;

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

const HierarchicalUnitChilds = (props) => {
    const record = useRecordContext(props);
    const show = (basePath, id, data) => `/hierarchicalunits/${data.hierarchicalUnitChildId}/show`;
    return record ?
        (
            <Fragment>
                <SectionTitle label="resources.hierarchicalunitrelationships.childs.name"/>
                <AssociateHierarchicalUnitChild {...props}/>
                <ReferenceManyField
                    addLabel={false}
                    reference="hierarchicalunitrelationships"
                    target="hierarchicalUnitParentId"
                >
                    <Datagrid rowClick={show}
                              empty={<p style={{paddingLeft:10, marginTop:0, color:'#8c8c8c'}}>Sin unidades jerárquicas hijas definidas</p>}>
                        <ReferenceField source="hierarchicalUnitChildId" label="resources.hierarchicalunits.fields.alias" reference="hierarchicalunits" link={false}>
                            <TextField source="alias"/>
                        </ReferenceField>
                        <DeleteButton redirect={false} disabled={!UserIsInstitutionalAdmin()}/>
                    </Datagrid>
                </ReferenceManyField>
            </Fragment> ) : null;
}

const HierarchicalUnitParents = (props) => {
    const record = useRecordContext(props);
    const show = (basePath, id, data) => `/hierarchicalunits/${data.hierarchicalUnitParentId}/show`;
    return record ?
        (
            <Fragment>
                <SectionTitle label="resources.hierarchicalunitrelationships.parents.name"/>
                <AssociateHierarchicalUnitParent {...props}/>
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

const AssociateHierarchicalUnitParent = ({ record }) => {
    const customRecord = {hierarchicalUnitChildId: record.id};
    return UserIsInstitutionalAdmin() ?( <CreateRelatedButton
            customRecord={customRecord}
            reference="hierarchicalunitrelationships"
            refFieldName="hierarchicalUnitChildId"
            label="resources.hierarchicalunitrelationships.parents.addRelated"
            />
    ) : null;
};
        
const HierarchicalUnitSectors = (props) => {
    const record = useRecordContext(props);
    const show = (basePath, id, data) => `/sectors/${data.sectorId}/show`;
    return record ?
        (
            <Fragment>
                <SectionTitle label="resources.hierarchicalunitsectors.sectors.name"/>
                <AsociateHierarchicalUnitSector {...props}/>
                <ReferenceManyField
                    addLabel={false}
                    reference="hierarchicalunitsectors"
                    target="hierarchicalUnitId"
                >
                    <Datagrid rowClick={show}
                              empty={<p style={{paddingLeft:10, marginTop:0, color:'#8c8c8c'}}>Sin sectores asociados</p>}>
                        <ReferenceField source="sectorId" label="resources.sectors.fields.description" reference="sectors" link={false}>
                            <TextField source="description"/>
                        </ReferenceField>
                        <DeleteButton redirect={false} disabled={!UserIsInstitutionalAdmin()}/>
                    </Datagrid>
                </ReferenceManyField>
            </Fragment>
        ) : null;
}

const AsociateHierarchicalUnitSector = ({ record }) => {
    const customRecord = {hierarchicalUnitId: record.id, institutionId: record.institutionId };
    return UserIsInstitutionalAdmin() ? ( <CreateRelatedButton
            customRecord={customRecord}
            reference="hierarchicalunitsectors"
            refFieldName="hierarchicalUnitId"
            label="resources.hierarchicalunitsectors.addRelated"
            />
    ) : null;
};


const AssociateHierarchicalUnitChild = ({ record }) => {
    const customRecord = {institutionId: record.institutionId, hierarchicalUnitIdToReport: record.id};
    return UserIsInstitutionalAdmin() ?(
        <CreateRelatedButton
            customRecord={customRecord}
            reference="hierarchicalunits"
            label="resources.hierarchicalunitrelationships.childs.createRelated"
        />
    ) : null;
};

const AssociateUserToHierarchicalUnit = ({ record }) => {
    const customRecord = {institutionId: record.institutionId, hierarchicalUnitId: record.id};
    return UserIsInstitutionalAdmin() ?( <CreateRelatedButton
            customRecord={customRecord}
            reference="hierarchicalunitstaff"
            refFieldName="hierarchicalUnitId"
            label="resources.hierarchicalunitstaff.addRelated"
        />
    ) : null;
};

const HierarchicalUnitStaff = (props) => {
    const record = useRecordContext(props);
    return record ?
        (
            <Fragment>
                <SectionTitle label="resources.hierarchicalunitstaff.name"/>
                <AssociateUserToHierarchicalUnit {...props}/>
                <ReferenceManyField
                    addLabel={false}
                    reference="hierarchicalunitstaff"
                    target="hierarchicalUnitId"
                >
                    <Datagrid empty={<p style={{paddingLeft:10, marginTop:0, color:'#8c8c8c'}}>Sin usuarios definidos</p>}>
                        <ReferenceField source="userId" reference="institutionuserpersons" label="resources.person.fields.firstName" link={false}>
                            <FunctionField render={renderPersonData}/>
                        </ReferenceField>
                        <ReferenceField source="userId" reference="institutionuserpersons" label="resources.person.fields.identificationNumber" link={false}>
                            <ReferenceField source="personId" reference="person" link={false}>
                                <TextField source="identificationNumber"/>
                            </ReferenceField>
                        </ReferenceField>
                        <BooleanField source="responsible"/>
                        <EditButton disabled={!UserIsInstitutionalAdmin()}/>
                    </Datagrid>
                </ReferenceManyField>
            </Fragment>
        ) : null;
}

const HierarchicalUnitShow = props => {
    const userIsInstitutionalAdmin = UserIsInstitutionalAdmin();
    return (
        <Show {...props} hasEdit={userIsInstitutionalAdmin}>
            <SimpleShowLayout>
                <TextField source="alias"/>
                <ReferenceField source="institutionId" reference="institutions">
                    <TextField source="name"/>
                </ReferenceField>
                <ReferenceField source="typeId" reference="hierarchicalunittypes" link={ false }>
                    <TextField source="description" />
                </ReferenceField>
                <ServiceField {...props} />
                <ReferenceField source="hierarchicalUnitIdToReport" link={userIsInstitutionalAdmin} reference="hierarchicalunits">
                    <TextField source="alias"/>
                </ReferenceField>
                <HierarchicalUnitChilds/>
                <HierarchicalUnitParents/>
                <HierarchicalUnitStaff/>
                <HierarchicalUnitSectors/>
            </SimpleShowLayout>
        </Show>
    );
}

export default HierarchicalUnitShow;

export { HierarchicalUnitChilds,  HierarchicalUnitParents, HierarchicalUnitStaff, renderPersonData, HierarchicalUnitSectors }
