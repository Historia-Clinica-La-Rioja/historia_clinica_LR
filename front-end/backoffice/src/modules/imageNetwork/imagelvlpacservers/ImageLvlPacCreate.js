import React from 'react';
import {
    TextInput,
    Create,
    SimpleForm,
    required,
    maxLength,
    NumberInput,
    maxValue
} from 'react-admin';
import CustomToolbar from '../../components/CustomToolbar';

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

                {/* URL del visualizador local*/}
                <TextInput 
                    source="localViewerUrl"
                    helperText={"Ingrese la URL del visualizador local. Ej: http://www.prueba.com.ar/?patient-id={dni}"}
                    validate={[maxLength(128)]}
                />


            </SimpleForm>
        </Create>
    );
};

export default ImageLvlPacCreate;
