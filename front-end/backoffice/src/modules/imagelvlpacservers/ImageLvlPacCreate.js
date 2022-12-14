import React from 'react';
import {
    TextInput,
    Create,
    SimpleForm,
    required,
    maxLength,
    NumberInput
} from 'react-admin';
import CustomToolbar from "../components/CustomToolbar";

const ImageLvlPacCreate = props => {
    return(
        <Create {...props} >
            <SimpleForm redirect="show" toolbar={<CustomToolbar/>}>

                {/* Name */}
                <TextInput source="name" validate={[
                    required(),
                    maxLength(20)]}
                />

                {/* AETITLE */}
                <TextInput source="aetitle" validate={[
                    required(),
                    maxLength(15)]}
                />

                {/* Domain */}
                <TextInput source="domain" validate={[
                    required(),
                    maxLength(50)]}
                />

                {/* Port */}
                <TextInput source={"port"} validate={[
                    required()]}
                />

                {/* Sector ID */}
                <NumberInput source={"sectorId"} disabled={true} validate={[
                    required()]}
                />

            </SimpleForm>
        </Create>
    );
};

export default ImageLvlPacCreate;
