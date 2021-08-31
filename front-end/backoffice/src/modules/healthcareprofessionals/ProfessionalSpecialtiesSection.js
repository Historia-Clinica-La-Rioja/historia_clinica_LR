import {Datagrid, DeleteButton, ReferenceField, ReferenceManyField, TextField} from "react-admin";
import React from "react";

const ProfessionalSpecialtiesSection = props => (
    <ReferenceManyField
        addLabel={false}
        reference="healthcareprofessionalspecialties"
        target="healthcareProfessionalId"
        {...props}
    >
        <Datagrid rowClick="show">
            <ReferenceField source="professionalSpecialtyId" reference="professionalspecialties">
                <TextField source="description"/>
            </ReferenceField>
            <ReferenceField source="clinicalSpecialtyId" reference="clinicalspecialties">
                <TextField source="name"/>
            </ReferenceField>
            <DeleteButton/>
        </Datagrid>
    </ReferenceManyField>
);

export default ProfessionalSpecialtiesSection;
