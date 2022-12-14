import React from 'react';
import {
    TextInput,
    Edit,
    SimpleForm,
    required,
    maxLength,
    usePermissions,
    NumberInput,
} from 'react-admin';
import CustomToolbar from "../components/CustomToolbar";
import { ROOT } from "../roles";

const ImageLvlPacEdit = props => {
    const { permissions } = usePermissions();
    const userIsRoot = permissions?.roleAssignments?.filter(roleAssignment => (roleAssignment.role === ROOT.role)).length > 0;
    return (<Edit {...props} hasEdit={userIsRoot}>
        <SimpleForm redirect="show" toolbar={<CustomToolbar />}>

            {/* Name */}
            <TextInput source="name" validate={[
                required(),
                maxLength(100)]}/>

            {/* AETITLE */}
            <TextInput source="aetitle" validate={[
                required(),
                maxLength(100)]}/>

            {/* Domain */}
            <TextInput source="domain" validate={[
                required(),
                maxLength(100)]}/>

            {/* Port */}
            <TextInput source={"port"} validate={[
                required()]}
            />

            {/* Sector ID */}
            <NumberInput source={"sectorId"} disabled={true} validate={[
                required()]}
            />

        </SimpleForm>
    </Edit>)
};

export default ImageLvlPacEdit;
