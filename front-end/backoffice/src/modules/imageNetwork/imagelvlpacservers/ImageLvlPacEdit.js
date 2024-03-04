import React from 'react';
import {
    TextInput,
    Edit,
    SimpleForm,
    required,
    maxLength,
    usePermissions,
    NumberInput, maxValue,
} from 'react-admin';
import CustomToolbar from '../../components/CustomToolbar';
import { ROOT } from '../../roles';

const ImageLvlPacEdit = props => {
    const { permissions } = usePermissions();
    const userIsRoot = permissions?.roleAssignments?.filter(roleAssignment => (roleAssignment.role === ROOT.role)).length > 0;
    return (<Edit {...props} hasEdit={userIsRoot}>
        <SimpleForm redirect="show" toolbar={<CustomToolbar isEdit={true} deleteRedirect={"/sectors"} />}>

            {/* Name */}
            <TextInput source="name" validate={[
                required(),
                maxLength(20)]}/>

            {/* AETITLE */}
            <TextInput source="aetitle" validate={[
                required(),
                maxLength(15)]}/>

            {/* Domain */}
            <TextInput source="domain" validate={[
                required(),
                maxLength(50)]}
                helperText={"Ingrese dirección IP o URL del dominio"}
            />

            {/* Port */}
            <NumberInput source={"port"} validate={[
                required(),
                maxValue(9999999999)]}
            />

            {/* Sector ID */}
            <NumberInput source={"sectorId"} disabled={true} validate={[
                required()]}
            />

        </SimpleForm>
    </Edit>)
};

export default ImageLvlPacEdit;
