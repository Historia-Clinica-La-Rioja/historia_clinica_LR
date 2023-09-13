import React, { useState } from 'react';
import { List, Datagrid, TextField, DeleteButton } from 'react-admin';
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
      </Tabs>
      {tabValue === 'generales' && (
        <List {...props} bulkActionButtons={false}>
          <Datagrid>
            <TextField label="Especialidad / Práctica o procedimiento" source="name" />
            <DeleteButton />
          </Datagrid>
        </List>
      )}
      {tabValue === 'locales'}
    </div>
  );
};

export default RuleList;