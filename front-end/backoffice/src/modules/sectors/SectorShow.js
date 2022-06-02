import React from 'react';
import {
    Datagrid,
    DeleteButton,
    EditButton,
    ReferenceField,
    ReferenceManyField,
    Show,
    SimpleShowLayout,
    TextField
} from 'react-admin';
import CreateRelatedButton from '../components/CreateRelatedButton';
import SectionTitle from '../components/SectionTitle';
import SgxDateField from "../../dateComponents/sgxDateField";

const CreateSector = ({ record }) => {
    return (
        <CreateRelatedButton
                customRecord={{ sectorId: record.id, institutionId: record.institutionId}}
                reference="sectors"
                label="resources.sectors.createRelated"
        />
    )
}

const SectorShow = props => (
    <Show {...props}>
        <SimpleShowLayout>
            <TextField source="description" />
            <ReferenceField source="institutionId" reference="institutions">
                <TextField source="name"/>
            </ReferenceField>

            <SectionTitle label="resources.sectors.fields.childSectors" />
            <CreateSector />
            <ReferenceManyField
                addLabel={false}
                reference="sectors"
                target= { "sectorId" }
                sort={{ field: 'description', order: 'DESC' }}
            >
                <Datagrid rowClick="show">
                    <TextField source="description" />
                    <EditButton />
                </Datagrid>
            </ReferenceManyField>

            <SectionTitle label="resources.sectors.fields.clinicalspecialtysectors"/>
            <CreateRelatedButton
                reference="clinicalspecialtysectors"
                refFieldName="sectorId"
                label="resources.clinicalspecialtysectors.createRelated"
            />
            <ReferenceManyField
                addLabel={false}
                reference="clinicalspecialtysectors"
                target="sectorId"
                sort={{ field: 'description', order: 'DESC' }}
            >
                <Datagrid rowClick="show">
                    <TextField source="description" />
                    <ReferenceField source="clinicalSpecialtyId" reference="clinicalspecialties">
                        <TextField source="name" />
                    </ReferenceField>
                    <DeleteButton />
                </Datagrid>
            </ReferenceManyField>

            <SectionTitle label="resources.clinicalspecialtysectors.fields.rooms"/>
            <CreateRelatedButton
                reference="rooms"
                refFieldName="sectorId"
                label="resources.rooms.createRelated"
            />
            <ReferenceManyField
                addLabel={false}
                reference="rooms"
                target="sectorId"
                sort={{ field: 'description', order: 'DESC' }}
            >
                <Datagrid rowClick="show">
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
