import {Datagrid, DeleteButton, ReferenceField, ReferenceManyField, TextField} from "react-admin";
import React from "react";

const redirect = (basePath) => {
    return `/professionalprofessions/${basePath.record.id}/show`;
};

const ProfessionalLicenseNumbersSection = props => (
    <ReferenceManyField
        addLabel={false}
        reference="healthcareprofessionallicensenumbers"
        target="professionalProfessionId"
        {...props}
    >
        <Datagrid rowClick="show" {...props}>
            <TextField source="licenseNumber" />
            <DeleteButton redirect={redirect(props)}/>
        </Datagrid>
    </ReferenceManyField>
);

export default ProfessionalLicenseNumbersSection;
