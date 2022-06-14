import {Datagrid, DeleteButton, ReferenceField, ReferenceManyField, TextField} from "react-admin";
import React from "react";

const redirect = (basePath) => {
    return `/professionalprofessions/${basePath.record.id}/show`;
};

const ProfessionalSpecialtiesSection = props => (
    <ReferenceManyField
        addLabel={false}
        reference="healthcareprofessionalspecialties"
        target="professionalProfessionId"
        {...props}
    >
        <Datagrid rowClick="show" {...props}>
            <ReferenceField source="clinicalSpecialtyId" reference="clinicalspecialties" link={false}>
                <TextField source="name"/>
            </ReferenceField>
            <DeleteButton redirect={redirect(props)}/>
        </Datagrid>
    </ReferenceManyField>
);

export default ProfessionalSpecialtiesSection;
