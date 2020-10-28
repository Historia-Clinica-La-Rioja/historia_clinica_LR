import React from 'react';
import {
    Show,
    SimpleShowLayout,
    ReferenceField,
    TextField,
    ReferenceManyField,
    Datagrid,
    DateField,
    EditButton,
    useGetOne,
    Loading
} from 'react-admin';
import SubReference from '../components/subreference';
import CreateRelatedButton from '../components/CreateRelatedButton';
import SectionTitle from '../components/SectionTitle';

const CreateDoctorsOffice = ({ record }) => {
    
    const { data, loading } = useGetOne('sectors', record.sectorId);

    if (loading) { 
        return <Loading />; 
    } else {
        const customRecord = {clinicalSpecialtySectorId: record.id, institutionId: data.institutionId};
        return (<CreateRelatedButton
            customRecord={customRecord}
            reference="doctorsoffices"
            refFieldName="clinicalSpecialtySectorId"
            label="resources.doctorsoffices.createRelated"/>)
    }
};

const ClinicalSpecialtySectorShow = props => (
    <Show {...props}>
        <SimpleShowLayout>
            <TextField source="description"/>
            <ReferenceField source="sectorId" reference="sectors" link={false} label="resources.sectors.fields.institutionId">
                <SubReference source="institutionId" reference="institutions" link={false}>
                    <TextField source="name"/>
                </SubReference>
            </ReferenceField>
            <ReferenceField source="sectorId" reference="sectors">
                <TextField source="description" />
            </ReferenceField>
            <ReferenceField source="clinicalSpecialtyId" reference="clinicalspecialties">
                <TextField source="name" />
            </ReferenceField>
            <SectionTitle label="resources.clinicalspecialtysectors.fields.rooms"/>
            <CreateRelatedButton
                reference="rooms"
                refFieldName="clinicalSpecialtySectorId"
                label="resources.rooms.createRelated"
            />
            <ReferenceManyField
                addLabel={false}
                reference="rooms"
                target="clinicalSpecialtySectorId"
                sort={{ field: 'description', order: 'DESC' }}
            >
                <Datagrid rowClick="show">
                    <TextField source="roomNumber" />
                    <TextField source="description"/>
                    <TextField source="type" />
                    <DateField source="dischargeDate" />
                    <EditButton />
                </Datagrid>
            </ReferenceManyField>

            <SectionTitle label="resources.clinicalspecialtysectors.fields.doctorsoffices"/>
            <CreateDoctorsOffice />
            <ReferenceManyField
                addLabel={false}
                reference="doctorsoffices"
                target="clinicalSpecialtySectorId"
                sort={{ field: 'description', order: 'DESC' }}
            >
                <Datagrid rowClick="show">
                    <TextField source="description"/>
                    <EditButton />
                </Datagrid>
            </ReferenceManyField>

        </SimpleShowLayout>
    </Show>
);

export default ClinicalSpecialtySectorShow;
