import React from 'react';
import {
    Show,
    TextField,
    DateField,
    SimpleShowLayout,
    ReferenceField,
} from 'react-admin';






const ClinicHistoryAuditShow = (props) => (
    <Show  {...props} >
        <SimpleShowLayout>
             <TextField source="firstName" />
              <TextField source="lastName" />
              <TextField source="description" />
              <TextField source="identificationNumber" />
              <TextField source="username" />
              <DateField source="date"/>
              <TextField source="reasonId" />
              <TextField source="institutionName" />
              <TextField source="observations" />
              <ReferenceField link={false} source="scope" reference="sectortypes">
                <TextField source="description" />
              </ReferenceField>
        </SimpleShowLayout>
    </Show>
);


export default ClinicHistoryAuditShow;
