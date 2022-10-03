import React from 'react';
import {
    Datagrid,
    DeleteButton,
    ReferenceField,
    ReferenceManyField,
    Show,
    SimpleShowLayout,
    TextField, useTranslate
} from 'react-admin';
import SectionTitle from '../components/SectionTitle';
import Button from "@material-ui/core/Button";
import { Link } from 'react-router-dom';

const CreateRelatedButton = ({record, reference, label, disabled}) => {
    const newRecord = {careLineInstitutionId: record.id, institutionId: record.institutionId, careLineId: record.careLineId};
    const translate = useTranslate();
    return (
        <Button
            component={Link}
            to={{
                pathname: `/${reference}/create`,
                state: { record: newRecord },
            }}
            disabled={disabled}
        >{translate(label)}</Button>
    );
};

const CareLineinstitutionShow = props => (
    <Show {...props}>
        <SimpleShowLayout>

            <ReferenceField source="institutionId" reference="institutions">
                <TextField source="name" />
            </ReferenceField>

            <ReferenceField source="careLineId" reference="carelines">
                <TextField source="description" />
            </ReferenceField>

            <SectionTitle label="Especialidades"/>
            <CreateRelatedButton
                reference="carelineinstitutionspecialty"
                label="Agregar Especialidad"
                disabled={false}
            />

            <ReferenceManyField
                addLabel={false}
                reference="carelineinstitutionspecialty"
                target="careLineInstitutionId"
            >
                <Datagrid>
                    <ReferenceField source="clinicalSpecialtyId" reference="clinicalspecialties">
                        <TextField source="name"/>
                    </ReferenceField>
                    <DeleteButton redirect={false}/>
                </Datagrid>
            </ReferenceManyField>

            <SectionTitle label="Practicas"/>
            <CreateRelatedButton
                reference="carelineinstitutionpractice"
                label="Agregar Practica"
                disabled={false}
            />

            <ReferenceManyField
                addLabel={false}
                reference="carelineinstitutionpractice"
                target="careLineInstitutionId"
            >
                <Datagrid>
                    <ReferenceField source="snomedRelatedGroupId" reference="practicesinstitution" link={false}>
                        <TextField source="description" />
                    </ReferenceField>
                    <DeleteButton redirect={false}/>
                </Datagrid>
            </ReferenceManyField>

        </SimpleShowLayout>
    </Show>
);

export default CareLineinstitutionShow;
