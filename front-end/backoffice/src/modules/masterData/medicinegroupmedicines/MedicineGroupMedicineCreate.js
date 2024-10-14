import { React, useState, useEffect, useRef } from "react";
import { AutocompleteInput, SimpleForm, Create, ReferenceInput, useGetOne } from "react-admin";
import CustomToolbar from '../../components/CustomToolbar';

const goBack = () => {
    window.history.back();
}

const MedicineGroupMedicineCreate = (props) => {
    
    const { data: record } = useGetOne('medicinegroups', props?.location?.state?.record?.medicineGroupId);

    const institutionId = props?.location?.state?.record?.institutionId;

    const reference = record?.isDomain ? "medicinefinancingstatus" : "institutionmedicinesfinancingstatus";

    const [medicine, setMedicine] = useState(null);

    const medicineRef = useRef(medicine); // Creamos un useRef para mantener la referencia al fármaco seleccionado

    // Transformar el valor que se envía en la solicitud
    const transform = (record) => {
        const selectedMedicine = medicineRef.current;
        const selectedMedicineId = (selectedMedicine.medicineId ? selectedMedicine.medicineId : selectedMedicine.id);
        // Construimos el objeto a enviar en la solicitud
        return {
            ...record,
            medicineId: selectedMedicineId , // Aquí enviamos el medicineId correcto
        };
    };

    useEffect(() => {
        medicineRef.current = medicine;
    }, [medicine]);

    return(
        <Create {...props} transform={transform}>

            <SimpleForm redirect={goBack} toolbar={<CustomToolbar />}>
            
                <ReferenceInput
                    source="medicineGroupId"
                    reference="medicinegroups"
                    label="Nombre del grupo">
                    <AutocompleteInput optionText="name" optionValue="id" disabled={true}></AutocompleteInput>
                </ReferenceInput>
            
                <ReferenceInput
                    source="medicineId"
                    reference={reference}
                    label="Fármaco"
                    filter={{ financed: true, institutionId: institutionId, medicineId: -1 }}
                    sort={{ field: 'name', order:'ASC' }}
                    perPage={100}         
                >
                    <AutocompleteInput optionText="conceptPt" onSelect={(selected) => setMedicine(selected)}> </AutocompleteInput>
                </ReferenceInput>
            
            </SimpleForm>
        
        </Create>
    );
} 

export default MedicineGroupMedicineCreate;