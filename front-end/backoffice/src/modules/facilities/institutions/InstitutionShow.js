import React, { Fragment } from 'react';
import {
    Show,
    SimpleShowLayout,
    ReferenceField,
    TextField,
    TopToolbar,
    EditButton,
    ReferenceManyField,
    Datagrid,
    ListButton,
    usePermissions,
    Tab,
    TabbedShowLayout,
    Pagination
} from 'react-admin';
import CreateRelatedButton from '../../components/CreateRelatedButton';
import SectionTitle from '../../components/SectionTitle';
import { ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE } from '../../roles';
import UnidadesJerarquicas from './UnidadesJerarquicas';

const InstitutionShowActions = ({ data }) => {
    return (!data || !data.id) ? <TopToolbar/> :
        (
            <TopToolbar>
                <ListButton basePath="/institutions" label="Listar Instituciones"/>
                <EditButton basePath="/institutions" record={{ id: data.id }} />
            </TopToolbar>
        )
};

const CreateHierarchicalUnit = ({ record }) => {
    const customRecord = {institutionId: record.id};
    return UserIsInstitutionalAdmin() ? (
        <CreateRelatedButton
            customRecord={customRecord}
            reference="hierarchicalunits"
            refFieldName="institutionId"
            label="resources.hierarchicalunits.createRelated"/>
    ) : null;
};

const UserIsInstitutionalAdmin = function () {
    const { permissions } = usePermissions();
    const userAdmin = permissions?.hasAnyAssignment(ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE);
    return userAdmin;
}

const ShowHierarchicalUnits = () => {
    return  (
    <ReferenceManyField
        id='hierarchicalunits'
        addLabel={false}
        reference="hierarchicalunits"
        target="institutionId"
        sort={{ field: 'alias', order: 'ASC' }}
        perPage={10}
        pagination={<Pagination/>}
    >
        <Datagrid rowClick="show"
                  empty={<p style={{paddingLeft:10, marginTop:0, color:'#8c8c8c'}}>Sin unidades jerárquicas definidas</p>}>
            <TextField sortable={false} source="id" />
            <TextField sortable={false} source="alias"/>
            <ReferenceField sortable={false} source="typeId" reference="hierarchicalunittypes" link={false}>
                <TextField source="description" />
            </ReferenceField>
            <EditButton disabled={!UserIsInstitutionalAdmin()}/>
        </Datagrid>
    </ReferenceManyField>
    );
};

const ShowSectors = () => {
    return (
        <ReferenceManyField
            id='sectors'
            addLabel={false}
            reference="rootsectors"
            target="institutionId"
            sort={{ field: 'description', order: 'ASC' }}
            filter={{ deleted: false }}
            perPage={10}
            pagination={<Pagination />}
        >
            <Datagrid rowClick="show"
                              empty={<p style={{paddingLeft:10, marginTop:0, color:'#8c8c8c'}}>Sin unidades sectores definidos</p>}>
                <TextField source="description" />
                <ReferenceField source="sectorTypeId"  link={false}  reference="sectortypes">
                    <TextField source="description" />
                </ReferenceField>
                <EditButton />
            </Datagrid>
        </ReferenceManyField>
    );
}

const InstitutionShow = props => {
    return (
        <Show actions={<InstitutionShowActions />} {...props}>
            <SimpleShowLayout>
                <TextField source="id" />
                <TextField source="name" />
                <TextField source="website" />
                <TextField source="phone" />
                <TextField source="email" />
                <TextField source="cuit" />
                <TextField source="sisaCode" />
                <ReferenceField source="dependencyId" reference="dependencies" link={false}>
                    <TextField source="description" />
                </ReferenceField>
                <TextField source="provinceCode" />
                <ReferenceField source="addressId" reference="addresses" link={'show'}>
                    <TextField source="cityId"/>
                </ReferenceField>
                <ReferenceField label="Latitud" source="addressId" reference="addresses" link={false}>
                    <TextField  source="latitude" />
                </ReferenceField>
                <ReferenceField label="Longitud" source="addressId" reference="addresses" link={false}>
                    <TextField  source="longitude" />
                </ReferenceField>
                <SectionTitle label="resources.institutions.fields.sectors"/>
                <CreateRelatedButton
                    reference="sectors"
                    refFieldName="institutionId"
                    label="resources.sectors.createRelated"
                />
                <ShowSectors />
                <SectionTitle label="resources.institutions.fields.hierarchicalUnits" />
                <CreateHierarchicalUnit />
               <HierarchicalUnitTabs {...props} />
            </SimpleShowLayout>
        </Show>
    );
}

const HierarchicalUnitTabs = (props) => (
    <Fragment>
        <TabbedShowLayout>
            <Tab label="Lista" id="lista">
                <ShowHierarchicalUnits />
            </Tab>
            <Tab label="Grafico" id="grafico">
                <UnidadesJerarquicas institutionId={props.id} />
            </Tab>
        </TabbedShowLayout>
    </Fragment>
)

export default InstitutionShow;
export { CreateHierarchicalUnit, UserIsInstitutionalAdmin, HierarchicalUnitTabs, ShowSectors };