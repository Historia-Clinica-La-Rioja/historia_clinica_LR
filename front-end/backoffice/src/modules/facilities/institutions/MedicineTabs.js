import React, { Fragment } from 'react';
import {
    Filter,
    TextInput,
    BooleanInput,
    List,
    BooleanField,
    DeleteButton,
    TextField,
    ReferenceManyField,
    Datagrid,
    Tab,
    TabbedShowLayout,
    Pagination,
    useRecordContext
} from 'react-admin';
import { CreateRelatedButton } from '../../components';

const MedicineFilter = (props) => (
    <Filter {...props}>
        <TextInput source="conceptSctid"/>
        <TextInput source="conceptPt"/>
        <BooleanInput source="financedByDomain"/>
        <BooleanInput source="financedByInstitution" />
    </Filter>
);

const EmptyTitle = () => <span />;

const ShowPharmacos = (props) => {    
    const institutionId = useRecordContext(props).id;
    return(
        <List {...props}
            resource="institutionmedicinesfinancingstatus"
            filter= {{ 'institutionId': institutionId }}
            perPage={10}
            filters={<MedicineFilter />}
            bulkActionButtons={false}
            exporter={false}
            hasCreate={false}
            title={<EmptyTitle />}
        >
            <Datagrid rowClick="edit">
                <TextField source="conceptSctid" />
                <TextField source="conceptPt" />
                <BooleanField source="financedByDomain" sortable={false} />
                <BooleanField source="financedByInstitution" sortable={false} />
            </Datagrid>
        </List>
    )
};

const DomainMedicineGroups = (props) => {
    return (
        <ReferenceManyField
            reference="institutionmedicinegroups"
            addLabel={false}
            target="institutionId"
            filter={{ 'isDomain' : true }}
            perPage={10}
            pagination={<Pagination />}
        >
            <Datagrid rowClick="edit">
                <TextField source="name" />
                <BooleanField source="enabled" sortable={false} />
            </Datagrid>
        </ReferenceManyField>
    )
};

const InstitutionMedicineGroups = (props) => {
    return (
        <ReferenceManyField
            reference="institutionmedicinegroups"
            target="institutionId"
            addLabel={false}
            perPage={10}
            filter={{ 'isDomain' : false}}
            pagination={<Pagination />}
        >
            <Datagrid rowClick="edit">
                <TextField source="name" label="Nombre"/>
                <DeleteButton  redirect={false}/>
            </Datagrid>
        </ReferenceManyField>
    )
};

const AddInstitutionMedicineGroup = (props) => {
    const customRecord = { institutionId: props.id };
    return (
        <CreateRelatedButton
            customRecord={customRecord}
            reference="institutionmedicinegroups"
            refFieldName="institutionId"
            label="resources.institutionmedicinegroups.addRelated"
        />
    );
};

export const MedicineTabs = (props) => (
    <Fragment>
        <TabbedShowLayout>
            <Tab label="Fármacos" id="pharmacos">
                <ShowPharmacos /> 
            </Tab>
            <Tab label="Grupos de fármacos" id="medicinegroups">
                <p>Grupos del dominio</p>
                <DomainMedicineGroups {...props} />
                <p>Grupos de la institución</p>
                <AddInstitutionMedicineGroup {...props} />
                <InstitutionMedicineGroups {...props} />
            </Tab>
        </TabbedShowLayout>
    </Fragment>
);