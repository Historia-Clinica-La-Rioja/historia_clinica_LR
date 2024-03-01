import React, {Fragment} from 'react';
import {
    TextField,
    useRecordContext,
    Datagrid,
    ReferenceField,
    DeleteButton,
    ReferenceManyField,
    FunctionField,
    SingleFieldList,
    ChipField,
    SelectField,
    ReferenceArrayField,
    EditButton,
    ShowButton,
} from 'react-admin';
import SectionTitle from '../../components/SectionTitle';
import CreateRelatedButton from '../../components/CreateRelatedButton';
import { TYPE_CHOICES_IDS } from '../proceduretemplateparameters/parameter-type';
import ChangeOrderButtons from './ChangeOrderButton';
import { procedureTemplateIsUpdateable } from './ProcedureTemplateStatus';

const AddParameter = ({ record }) => {
    if (!record) return null;
    return record ? ( <CreateRelatedButton
            customRecord={{procedureTemplateId: record.id}}
            record={{procedureTemplateId: record.id}}
            reference="proceduretemplateparameters"
            refFieldName="otroVal"
            label="resources.proceduretemplateparameters.addRelated"
            />
    ) : null;
  
};

const EditParameter = (props) => {
    const record = useRecordContext(props);
    if (!record || !record.id) return null;
    return (<EditButton
        redirect={false} 
        basePath=""
        record={{id: record.id}}
        resource={'proceduretemplateparameters'}
        undoable={false}
        confirmTitle='resources.proceduretemplateparameters.editRelated'
    />);
}

const ShowParameter = (props) => {
    const record = useRecordContext(props);
    if (!record || !record.id) return null;
    return (<ShowButton
        redirect={false} 
        basePath=""
        record={{id: record.id}}
        resource={'proceduretemplateparameters'}
        undoable={false}
        confirmTitle='resources.proceduretemplateparameters.editRelated'
    />);
}

const DeleteParameter = (props) => {
    const record = useRecordContext(props);
    if (!record || !record.id) return null;
    return (<DeleteButton
        redirect={false} 
        basePath=""
        record={{id: record.id}}
        resource={'proceduretemplateparameters'}
        undoable={false}
        confirmTitle='resources.proceduretemplateparameters.deleteRelated'
    />);
}

const AssociatedParametersDataGrid = ({canEdit, ...props}) => {
    return (
        <ReferenceManyField
        addLabel={false}
        reference="proceduretemplateparameters"
        target="procedureTemplateId"
        sort={{ field: 'orderNumber', order: 'ASC' }}
        >
            <Datagrid
                empty={<p style={{paddingLeft:10, marginTop:0, color:'#8c8c8c'}}>Sin parámetros asociados</p>}
            >
                
                {/**
                 * Description
                */}
                <ReferenceField
                    source="loincId"
                    reference="loinc-codes"
                    link={false}
                    label='resources.proceduretemplateparameters.fields.description'
                    sortable={false}
                >
                    <FunctionField render={(x) => x?.customDisplayName || x?.displayName || x?.description || ''} />
                </ReferenceField>

                {/**
                 * LOINC Code
                */}
                <ReferenceField
                    source="loincId"
                    reference="loinc-codes"
                    link={false}
                    sortable={false}
                >
                    <TextField source="code"/>
                </ReferenceField>

                {/**
                 * Type
                */}
                <SelectField
                    source='typeId'
                    choices={TYPE_CHOICES_IDS}
                    sortable={false}
                />

                {/**
                 * Units of measure
                */}
                <ReferenceArrayField
                    label="resources.proceduretemplateparameters.fields.unitsOfMeasureIds"
                    reference="units-of-measure"
                    source="unitsOfMeasureIds"
                    sortable={false}
                >
                    <SingleFieldList>
                        <ChipField source="code" />
                    </SingleFieldList>
                </ReferenceArrayField>
                {canEdit && <ChangeOrderButtons/>}
                {(canEdit && <EditParameter/>) || <ShowParameter/>}
                {canEdit && <DeleteParameter/>}
            </Datagrid>
        </ReferenceManyField>
    );

}

export const AssociatedParameters = (props) => {
    const record = useRecordContext(props);
    return (
            <Fragment>
                <SectionTitle label="resources.proceduretemplates.fields.associatedParameters"/>
                {record && procedureTemplateIsUpdateable(record.statusId) && <AddParameter {...props} />}
                <AssociatedParametersDataGrid canEdit={procedureTemplateIsUpdateable(record.statusId)}/>
            </Fragment>
        );
}
