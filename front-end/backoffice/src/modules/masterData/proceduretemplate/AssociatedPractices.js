import React, {Fragment} from 'react';
import {
    TextField,
    useRecordContext,
    Datagrid,
    ReferenceField,
    DeleteButton
} from 'react-admin';
import SectionTitle from '../../components/SectionTitle';
import CreateRelatedButton from '../../components/CreateRelatedButton';
import { procedureTemplateIsUpdateable } from './ProcedureTemplateStatus';

const AddPractice = ({ record }) => {
    if (!record) return null;
    return record ? ( <CreateRelatedButton
            customRecord={{id: record.id}}
            record={{id: record.id}}
            reference="proceduretemplatesnomeds"
            refFieldName="otroVal"
            label="resources.proceduretemplatesnomeds.addRelated"
            />
    ) : null;
  
};

const DeletePractice = ({procedureTemplateId, ...props}) => {
    const record = useRecordContext(props);
    if (!record || !procedureTemplateId) return null;
    return (<DeleteButton
        redirect={false} 
        basePath=""
        record={{id: `${procedureTemplateId}/${record.id}`}}
        resource={'proceduretemplatesnomeds'}
        undoable={false}
        confirmTitle='resources.proceduretemplatesnomeds.deleteRelated'
    />);
}

const AssociatedPracticesDataGrid = ({canEdit, ...props}) => {
    const record = useRecordContext(props);
    if (!record) return null;

    const data = record?.associatedPractices?.reduce((acc, curr)=> {acc[curr.id]=curr; return acc}, {});
    const ids = record?.associatedPractices?.map(ap=>ap.id);
  
    return (
        <Datagrid
            rowClick={() => {}}
            empty={<p style={{paddingLeft:10, marginTop:0, color:'#8c8c8c'}}>Sin prácticas asociadas</p>}
            data={data || {}}
            ids={ids || []}
            currentSort={{ field: 'pt', order: 'DESC' }}
            basePath=""
            selectedIds={[]}
            loaded={record}
            total={record?.associatedPractices?.length}
            setSort={() => {}}
            onSelect={() => {}}
            onToggleItem={() => {}}
        >
            <TextField source="pt" label="Descripción"/>
            <TextField source="sctid" label="SNOMED"/>
            {canEdit && <DeletePractice procedureTemplateId={record.id}/>}
        </Datagrid>
    );
}


export const AssociatedPractices = (props) => {
    const record = useRecordContext(props);
    const canEdit = procedureTemplateIsUpdateable(record.statusId)
    return record ?
        (
            <Fragment>
                <SectionTitle label="resources.proceduretemplates.fields.associatedPractices"/>
                {canEdit && <AddPractice {...props} />}
                <ReferenceField
                source="id" link={false} sortable={false}
                reference="proceduretemplatesnomeds"
                label="resources.proceduretemplates.fields.associatedPractices">
                    <AssociatedPracticesDataGrid canEdit={canEdit}/>
                </ReferenceField>
            </Fragment>
        ) : null;
}