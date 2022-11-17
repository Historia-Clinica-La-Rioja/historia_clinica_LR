import React from 'react';
import {
    Datagrid,
    DeleteButton,
    EditButton, Labeled,
    ReferenceField,
    ReferenceManyField,
    Show,
    SimpleShowLayout,
    TextField, useRecordContext
} from 'react-admin';
import CreateRelatedButton from '../components/CreateRelatedButton';
import SectionTitle from '../components/SectionTitle';
import SgxDateField from "../../dateComponents/sgxDateField";

const AMBULATORIA = 1;
const INTERNACION = 2;
const GUARDIA = 3;

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

const ShowServiceSectorData = ({ record }) => {
    if (record?.sectorOrganizationId === 1 && record?.sectorTypeId === 2)
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
            >
                <Datagrid rowClick="show">
                    <TextField source="description" />
                    <ReferenceField source="sectorTypeId"  link={false}  reference="sectortypes">
                        <TextField source="description" />
                    </ReferenceField>
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
            >
                <Datagrid rowClick="show"
                          empty={<p style={{paddingLeft:10, marginTop:0, color:'#8c8c8c'}} >Sin consultorios definidos</p>}>
                    <TextField source="description"/>
                    <EditButton />
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
            
        </SimpleShowLayout>
    </Show>
);

export default SectorShow;
export { CreateSector, CreateDoctorsOffice, CreateRooms, ShowServiceSectorData};
