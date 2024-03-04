import {Datagrid, DeleteButton, ReferenceField, ReferenceManyField, TextField} from "react-admin";
import React from "react";

const redirect = (basePath) => {
    return `/healthcareprofessionalspecialties/${basePath.record.id}/show`;
};

const ProfessionalSpecialtyLicenseNumbersSection = (props) => (
    <ReferenceManyField
        addLabel={false}
        reference="healthcareprofessionalspecialtylicensenumbers"
        target="healthcareProfessionalSpecialtyId"
        {...props}
    >
        <Datagrid rowClick="show" {...props}>
            <TextField source="licenseNumber" />
            <ReferenceField source="typeId" reference="licensenumbertypes" link={false}>
                <TextField source="description" />
            </ReferenceField>
            <DeleteButton redirect={redirect(props)} />
        </Datagrid>
    </ReferenceManyField>
);

export default ProfessionalSpecialtyLicenseNumbersSection;
