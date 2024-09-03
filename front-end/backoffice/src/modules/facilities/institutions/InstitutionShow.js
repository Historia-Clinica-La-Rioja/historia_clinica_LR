import React, { Fragment, useState } from 'react';
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
import { Grid, Divider } from '@material-ui/core';
import { MedicineTabs } from './MedicineTabs';
import {Button} from '@material-ui/core';
import ArrowForwardIosIcon from '@material-ui/icons/ArrowForwardIos';
import ArrowBackIcon from '@material-ui/icons/ArrowBack';

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
    const { permissions } = usePermissions();
    const parameterizedFormFF = permissions?.featureFlags.some( ff => ff === 'HABILITAR_FORMULARIOS_CONFIGURABLES_EN_DESARROLLO');

    const [showButtons, setShowButtons] = useState(true);
    const [showSectors, setShowSectors] = useState(false);
    const [showHierarchicalUnit, setHierarchicalUnit] = useState(false);
    const [showParameterizedForm, setShowParameterizedForm] = useState(false);
    const [showPharmacosSection, setPharmacosSection] = useState(false);

    const toggleShowSectors = () => {
        setShowSectors(true);
        setShowButtons(false);
    };

    const toggleShowHierarchicalUnit = () => {
        setHierarchicalUnit(true);
        setShowButtons(false);
    };

    const toggleShowParameterizedForm = () => {
        setShowParameterizedForm(true);
        setShowButtons(false);
    };

    const toggleShowPharmacosSection = () => {
        setPharmacosSection(true);
        setShowButtons(false);
    };

    const resetShowSections = () => {
        setShowSectors(false);
        setShowButtons(true);
        setHierarchicalUnit(false);
        setShowParameterizedForm(false);
        setPharmacosSection(false);
    };

    const ActionButton = ({ onClick, label }) => (
        <Button color="primary" onClick={onClick} style={{marginBottom : 10, marginTop : 10}}>
            {label} &nbsp; <ArrowForwardIosIcon fontSize='inherit'/>
        </Button>
    );

    const BackButton = () => {
        return (
            <Button color="primary" onClick={resetShowSections} variant='outlined' style={{marginTop : 10}}>
                <ArrowBackIcon fontSize='inherit'/> &nbsp; Volver
            </Button>
        );
    }

    const SectorsSection = ({ record }) => {
        return (
            <>
                <SectionTitle label="resources.institutions.fields.sectors" />
                    <CreateRelatedButton
                        customRecord={{ institutionId: record.id}}
                        reference="sectors"
                        label="resources.sectors.createRelated"
                    />
                <ShowSectors />
                <BackButton />
            </>
        );
    }

    const HierarchicalUnitSection = (props) => {
        return (
            <>
                <SectionTitle label="resources.institutions.fields.hierarchicalUnits" />
                <CreateHierarchicalUnit {...props} />
                <HierarchicalUnitTabs {...props} />
                <BackButton />
            </>
        );
    }

    const PharmacosSection = () => {
        return (
            <>
                <SectionTitle label="resources.institutions.fields.pharmacos" />
                <MedicineTabs {...props}/>
                <BackButton />
            </>
        );
    }

    const ParameterizedFormsSection = (props) => {
        return (
            <>
                <BackButton />
            </>
        );
    }

    return (
        <Show actions={<InstitutionShowActions />} {...props}>
            <SimpleShowLayout>
                <Grid container spacing={2}>
                    {/* Primer fila */}
                    <Grid item xs={3}>
                        <SimpleShowLayout>
                        <TextField source="name" label="Nombre"/>
                        </SimpleShowLayout>
                    </Grid>

                    <Grid item xs={3}>
                        <SimpleShowLayout>
                            <TextField source="id" />
                        </SimpleShowLayout>
                    </Grid>

                    <Grid item xs={3}>
                        <SimpleShowLayout>
                            <TextField source="sisaCode" label="Código SISA"/>
                        </SimpleShowLayout>
                    </Grid>

                    <Grid item xs={3}>
                        <SimpleShowLayout>
                            <ReferenceField source="addressId" reference="addresses" link={'show'} label="Dirección">
                                <TextField source="cityId"/>
                            </ReferenceField>
                        </SimpleShowLayout>
                    </Grid> 
                    
                    <Grid item xs={12}><Divider /></Grid>

                    {/* Segunda fila */}

                    <Grid item xs={3}>
                        <SimpleShowLayout>
                            <TextField source="phone" label="Teléfono"/>
                        </SimpleShowLayout>
                    </Grid>
                    
                    <Grid item xs={3}>
                        <SimpleShowLayout>
                            <TextField source="email" />
                        </SimpleShowLayout>
                    </Grid>

                    <Grid item xs={3}>
                        <SimpleShowLayout>
                            <ReferenceField source="dependencyId" reference="dependencies" link={false} label="Dependencia">
                                <TextField source="description" />
                            </ReferenceField>
                        </SimpleShowLayout>
                    </Grid>

                    <Grid item xs={3}>
                        <SimpleShowLayout>
                            <ReferenceField label="Latitud" source="addressId" reference="addresses" link={false}>
                                <TextField  source="latitude" />
                            </ReferenceField>
    
                            <ReferenceField label="Longitud" source="addressId" reference="addresses" link={false}>
                                <TextField  source="longitude" />
                            </ReferenceField>
                        </SimpleShowLayout>

                    </Grid>
                    
                    <Grid item xs={12}><Divider /></Grid>

                    {/* Tercer fila */}

                    <Grid item xs={3}>
                        <SimpleShowLayout>
                            <TextField source="website" label="Sitio web" />
                        </SimpleShowLayout>
                    </Grid>

                    <Grid item xs={3}>
                        <SimpleShowLayout>
                            <TextField source="cuit" />
                        </SimpleShowLayout>
                    </Grid>

                    <Grid item xs={3}>
                        <SimpleShowLayout>
                            <TextField source="provinceCode" />
                        </SimpleShowLayout>
                    </Grid>
                      
                </Grid>

                {showButtons && <ActionButton onClick={toggleShowSectors} label="Sectores" />}
                {showButtons && <ActionButton onClick={toggleShowHierarchicalUnit} label="Unidades jerárquicas" />}
                {(parameterizedFormFF && showButtons) && <ActionButton onClick={toggleShowParameterizedForm} label="Formularios configurables" />}
                {showButtons && <ActionButton onClick={toggleShowPharmacosSection} label="Fármacos" />}
                {showSectors && <SectorsSection/>}
                {showHierarchicalUnit && <HierarchicalUnitSection {...props} />}
                {showParameterizedForm && <ParameterizedFormsSection {...props} />}
                {showPharmacosSection && <PharmacosSection/>}

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
);

export default InstitutionShow;
export { CreateHierarchicalUnit, UserIsInstitutionalAdmin, HierarchicalUnitTabs, ShowSectors };