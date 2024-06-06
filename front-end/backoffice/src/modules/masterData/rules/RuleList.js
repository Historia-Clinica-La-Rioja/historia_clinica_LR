import React, { useState } from 'react';
import { List, Datagrid, TextField, DeleteButton, ReferenceField } from 'react-admin';
import { Tabs, Tab as MuiTab } from '@material-ui/core';


const RuleList = (props) => {
  const [tabValue, setTabValue] = useState('generales');

  const handleChangeTab = (event, newValue) => {
    setTabValue(newValue);
  };

  return (
    <div>
      <Tabs value={tabValue} onChange={handleChangeTab} variant="fullWidth">
        <MuiTab label="Generales" value="generales" />
        <MuiTab label="Locales" value="locales" />
      </Tabs>
      {tabValue === 'generales' && (
        <List {...props} bulkActionButtons={false}>
          <Datagrid>
            <TextField label="Especialidad / Práctica o procedimiento" source="name" />
            <DeleteButton />
          </Datagrid>
        </List>
      )}
      {tabValue === 'locales' && (
        <List {...props} hasCreate={false} resource="institutionalgroups" bulkActionButtons={false}>
          <Datagrid rowClick={(id, basePath, resource) => `/institutionalgroups/${resource.id}/show/2`} > 
              <TextField source="name" label='Nombre de grupo' />
              <ReferenceField source="typeId" reference="institutionalgrouptypes" label='Tipo de grupo' sortable={false} link={false}>
                  <TextField source="value"/>
              </ReferenceField>
              <TextField source="institutions" label='Instituciones' link={false} sortable={false} />
          </Datagrid>
        </List>
      )}
    </div>
  );
};

export default RuleList;