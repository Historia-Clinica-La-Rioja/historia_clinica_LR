import React from 'react';
import {
    Datagrid,
    DeleteButton,
    EditButton, Labeled,
    ReferenceField,
    ReferenceManyField,
    Show,
    SimpleShowLayout,
    BooleanField,
    TextField, useRecordContext,
    usePermissions
} from 'react-admin';
import {
    SgxDateField,
    CreateRelatedButton,
    SectionTitle,
} from '../../components';
import {
    ROOT,
    ADMINISTRADOR,
    ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE,
} from '../../roles';

const AMBULATORIA = 1;
const INTERNACION = 2;
const GUARDIA = 3;
const DIAGNOSTICO_POR_IMAGENES = 4;

const CreateSector = ({ record }) => {
    return (
        <CreateRelatedButton
                customRecord={{ sectorId: record.id, institutionId: record.institutionId}}
                reference="sectors"
                label="resources.sectors.createRelated"
        />
    )
}

const CreateDoctorsOffice = ({ record }) => {
    const customRecord = {sectorId: record.id, institutionId: record.institutionId};
    return record.sectorTypeId === AMBULATORIA ||
        record.sectorTypeId === GUARDIA ?(<CreateRelatedButton
        customRecord={customRecord}
        reference="doctorsoffices"
        refFieldName="sectorId"
        label="resources.doctorsoffices.createRelated"/>
    ) : null;
};

const CreateShockroom = ({ record }) => {
    const customRecord = {sectorId: record.id, institutionId: record.institutionId};
    return record.sectorTypeId === GUARDIA ?(<CreateRelatedButton
        customRecord={customRecord}
        reference="shockroom"
        refFieldName="sectorId"
        label="resources.shockroom.createRelated"/>
    ) : null;
};

const UserIsAdmin = function () {
    const { permissions } = usePermissions();
    const userAdmin= permissions?.roleAssignments?.filter(roleAssignment => (roleAssignment.role === ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE.role ||
                    roleAssignment.role === ADMINISTRADOR.role )).length > 0;
 return userAdmin;
}

const CreateOrchestrator = ({ record }) => {
    const { permissions } = usePermissions();
    const userCanView = permissions?.roleAssignments?.filter(roleAssignment => (roleAssignment.role === ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE.role ||
                roleAssignment.role === ADMINISTRADOR.role || roleAssignment.role === ROOT.role)).length > 0;
    const customRecord = {sectorId: record.id};
    let button = null;
    if (UserIsAdmin()){
        button= <CreateRelatedButton customRecord={customRecord} reference="orchestrator" refFieldName="sectorId" label="resources.orchestrator.createRelated" />;
    }
    return record.sectorTypeId === DIAGNOSTICO_POR_IMAGENES && userCanView?(
    <>
    <SectionTitle label="resources.orchestrator.name"/>
    {button}
    </>
    ) : null;
};

const CreateEquipment = ({ record }) => {
    const { permissions } = usePermissions();
    const userCanView = permissions?.roleAssignments?.filter(roleAssignment => (roleAssignment.role === ADMINISTRADOR_INSTITUCIONAL_BACKOFFICE.role ||
                roleAssignment.role === ADMINISTRADOR.role || roleAssignment.role === ROOT.role)).length > 0;
    const customRecord = {sectorId: record.id};
    let button = null;
    if (UserIsAdmin()){
        button= <CreateRelatedButton customRecord={customRecord} reference="equipment" refFieldName="sectorId" label="resources.equipment.createRelated" />;
    }
    return record.sectorTypeId === DIAGNOSTICO_POR_IMAGENES && userCanView?(
    <>
    <SectionTitle label="resources.equipment.name"/>
    {button}
    </>
    ) : null;
};

const RenderModality = (data ) => {
    const modality = data.record;
    return  (<>{modality.acronym} ({modality.description})</>);
};

const CreateRooms = ({ record }) => {
    const customRecord = {sectorId: record.id, institutionId: record.institutionId};
    return record.sectorTypeId === INTERNACION ||
    record.sectorTypeId === GUARDIA ? (
        <CreateRelatedButton
            customRecord={customRecord}
            reference="rooms"
            refFieldName="sectorId"
            label="resources.rooms.createRelated"
        />
    ) : null;
}

const CreatePacServer = ({ record }) => {
    const customRecord = {sectorId: record.id};
    return record.sectorTypeId === DIAGNOSTICO_POR_IMAGENES ? (
            <>
                <SectionTitle label="resources.pacserversimagelvl.name"/>
                <CreateRelatedButton
                    customRecord={customRecord}
                    reference="pacserversimagelvl"
                    refFieldName="sectorId"
                    label="resources.pacserversimagelvl.createRelated"
                />
            </>
        ) : null;
}

const SectorTypeField = (props) => {
    const record = useRecordContext(props);
    return record && record.sectorTypeId != null
        ?
        <Labeled label="resources.sectors.fields.sectorTypeId">
        <ReferenceField link={false} source="sectorTypeId" reference="sectortypes"
                        {...props}>
            <TextField source="description"/>
        </ReferenceField>
        </Labeled>
        : null;
}

const RootSectorField = (props) => {
    const record = useRecordContext(props);
    return record && record.sectorTypeId != null
        ?
        <Labeled label="resources.sectors.fields.sectorId">
            <ReferenceField source="sectorId" reference="sectors"
                            {...props}>
                <TextField source="description"/>
            </ReferenceField>
        </Labeled>
        : null;
}

const HospitalizationField = (props) => {
    const record = useRecordContext(props);
    const label = "resources.sectors.fields." + props.source;
    return record.sectorTypeId !== INTERNACION ? null : (
        <Labeled label={label}>
            <ReferenceField link={false} {...props}>
                <TextField source="description"/>
            </ReferenceField>
        </Labeled>
    )
}

const CheckBoxField = (props) => {
    const record = useRecordContext(props);
    return record.sectorTypeId !== DIAGNOSTICO_POR_IMAGENES ? null : (
        <Labeled label="resources.sectors.fields.informer">
                    <ReferenceField link={false}source="id" reference="sectors">
                        <BooleanField source="informer" />
                    </ReferenceField>
        </Labeled>
    )

}

const ShowServiceSectorData = ({ record }) => {
    if ((record?.sectorOrganizationId === 1 && record?.sectorTypeId === 2) || (record?.sectorOrganizationId === 1 && record?.sectorTypeId === 3) )
        return (
            <>
                <SectionTitle label="resources.clinicalservicesectors.name"/>
                <CreateRelatedButton
                    reference="clinicalservicesectors"
                    refFieldName="sectorId"
                    label="resources.clinicalservicesectors.createRelated"
                    record={ record }
                />
                <ReferenceManyField
                    id='clinical-service-sectors'
                    addLabel={false}
                    reference="clinicalservicesectors"
                    target="sectorId"
                    sort={{ field: 'description', order: 'DESC' }}
                    perPage={100}
                >
                    <Datagrid rowClick="show">
                        <TextField source="description" />
                        <ReferenceField source="clinicalSpecialtyId" reference="clinicalservices">
                            <TextField source="name" />
                        </ReferenceField>
                        <DeleteButton />
                    </Datagrid>
                </ReferenceManyField>
            </>
        )
    return (
        <>
            <SectionTitle label="resources.clinicalservicesectors.name"/>
            <ReferenceManyField
                    id='clinical-service-sectors'
                    addLabel={false}
                    reference="clinicalservicesectors"
                    target="sectorId"
                    sort={{ field: 'description', order: 'DESC' }}
                    perPage={100}
            >
                <Datagrid rowClick="show">
                    <TextField source="description" />
                    <ReferenceField source="clinicalSpecialtyId" reference="clinicalservices">
                        <TextField source="name" />
                    </ReferenceField>
                    <DeleteButton />
                </Datagrid>
            </ReferenceManyField>
        </>
    );
}

const SectorShow = props => (
    <Show {...props}>
        <SimpleShowLayout>
            <TextField source="description" />
            <ReferenceField source="institutionId" reference="institutions">
                <TextField source="name"/>
            </ReferenceField>
            <RootSectorField/>
            <SectorTypeField/>

            <CheckBoxField />
            
            <HospitalizationField {...props} reference="agegroups" source="ageGroupId"/>
            <HospitalizationField {...props} reference="sectororganizations" source="sectorOrganizationId"/>
            <HospitalizationField {...props} reference="caretypes" source="careTypeId"/>
            <HospitalizationField {...props} reference="hospitalizationtypes" source="hospitalizationTypeId"/>
            
            <ReferenceField source="sectorOrganizationId" reference="sectororganizations" link={ false }>
                <TextField source="description" />
            </ReferenceField>
            
            <SectionTitle label="resources.sectors.fields.childSectors" />
            <CreateSector />
            <ReferenceManyField
                id='child-sectors'
                addLabel={false}
                reference="sectors"
                target= { "sectorId" }
                sort={{ field: 'description', order: 'DESC' }}
                filter={{ deleted: false }}
                perPage={100}
            >
                <Datagrid rowClick="show">
                    <TextField source="description" />
                    <ReferenceField source="sectorTypeId" link={false} reference="sectortypes">
                        <TextField source="description" />
                    </ReferenceField>
                    <EditButton />
                </Datagrid>
            </ReferenceManyField>

            <CreatePacServer />
            <ReferenceManyField
                addLabel={false}
                reference="pacserversimagelvl"
                target= { "sectorId" }
                sort={{ field: 'name', order: 'DESC' }}
                perPage={100}
            >
                <Datagrid rowClick="show">
                    <TextField source="name" />
                    <TextField source="aetitle" />
                    <TextField source="domain" />
                    <TextField source="port" />
                    <TextField source="localViewerUrl" />
                    <EditButton />
                </Datagrid>
            </ReferenceManyField>

            <SectionTitle label="resources.clinicalspecialtysectors.fields.doctorsoffices"/>
            <CreateDoctorsOffice />
            <ReferenceManyField
                id='doctors-offices'
                addLabel={false}
                reference="doctorsoffices"
                target="sectorId"
                sort={{ field: 'description', order: 'DESC' }}
                filter={{ deleted: false }}
                perPage={100}
            >
                <Datagrid rowClick="show"
                          empty={<p style={{paddingLeft:10, marginTop:0, color:'#8c8c8c'}} >Sin consultorios definidos</p>}>
                    <TextField source="description"/>
                    <EditButton />
                </Datagrid>
            </ReferenceManyField>

            <CreateOrchestrator />
            <ReferenceManyField
                id='orchestrator'
                addLabel={false}
                reference="orchestrator"
                target="sectorId"
                sort={{ field: 'name', order: 'DESC' }}
                perPage={100}
                >
                <Datagrid rowClick={UserIsAdmin()?"show":""}>
                    <TextField source="name"/>
                    <TextField source="baseTopic"/>
                    <EditButton disabled= {!UserIsAdmin()}/>
                    <DeleteButton disabled= {!UserIsAdmin()}/>
                </Datagrid>
            </ReferenceManyField>

            <CreateEquipment />
            <ReferenceManyField
                id='equipment'
                addLabel={false}
                reference="equipment"
                target="sectorId"
                sort={{ field: 'aeTitle', order: 'DESC' }}
                perPage={100}
            >
                <Datagrid rowClick={UserIsAdmin()?"show":""}>
                    <TextField source="name" />
                    <TextField source="aeTitle" />
                    <ReferenceField link={false} source="pacServerId"  reference="pacserversimagelvl">
                        <TextField  source="name" />
                    </ReferenceField>
                    <ReferenceField link={false}source="orchestratorId" reference="orchestrator">
                        <TextField  source="name" />
                    </ReferenceField>
                    <ReferenceField link={false}source="modalityId" reference="modality">
                        <RenderModality/>
                    </ReferenceField>
                        <EditButton disabled= {!UserIsAdmin()}/>
                        <DeleteButton disabled= {!UserIsAdmin()}/>
                </Datagrid>
            </ReferenceManyField>

            <ShowServiceSectorData />

            <SectionTitle label="resources.clinicalspecialtysectors.fields.rooms"/>
            <CreateRooms/>
            <ReferenceManyField
                id='rooms'
                addLabel={false}
                reference="rooms"
                target="sectorId"
                sort={{ field: 'description', order: 'DESC' }}
                perPage={100}
            >
                <Datagrid rowClick="show"
                          empty={<p style={{paddingLeft:10, marginTop:0, color:'#8c8c8c'}}>Sin habitaciones definidas</p>}>
                    <TextField source="roomNumber" />
                    <TextField source="description"/>
                    <TextField source="type" />
                    <SgxDateField source="dischargeDate" />
                    <EditButton />
                </Datagrid>
            </ReferenceManyField>
            
            <SectionTitle label="resources.shockroom.name"/>
            <CreateShockroom/>
            <ReferenceManyField
                id='shockroom'
                addLabel={false}
                reference="shockroom"
                target="sectorId"
                sort={{ field: 'description', order: 'DESC' }}
                filter={{ deleted: false }}
                perPage={100}
            >
                <Datagrid rowClick="show"
                          empty={<p style={{paddingLeft:10, marginTop:0, color:'#8c8c8c'}}>Sin Shockrooms definidos</p>}>
                    <TextField source="description"/>
                    <EditButton />
                </Datagrid>
            </ReferenceManyField>
        </SimpleShowLayout>
    </Show>
);

export default SectorShow;
export { CreateSector, CreateDoctorsOffice, CreateRooms, ShowServiceSectorData, CreateOrchestrator, UserIsAdmin, CreateEquipment, RenderModality, CreateShockroom};
