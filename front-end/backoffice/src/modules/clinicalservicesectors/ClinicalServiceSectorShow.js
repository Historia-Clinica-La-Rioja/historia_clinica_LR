import React from 'react';
import {
    ReferenceField,
    Show,
    SimpleShowLayout,
    TextField,
} from 'react-admin';
import SubReference from '../components/subreference';

const ClinicalServiceSectorShow = props => (
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
            <ReferenceField source="clinicalSpecialtyId" reference="clinicalservices">
                <TextField source="name" />
            </ReferenceField>

        </SimpleShowLayout>
    </Show>
);

export default ClinicalServiceSectorShow;
