import React, { useState, useEffect } from 'react';

import Button from '@material-ui/core/Button';
import DialogTitle from '@material-ui/core/DialogTitle';
import Dialog from '@material-ui/core/Dialog';
import Radio from '@material-ui/core/Radio';
import RadioGroup from '@material-ui/core/RadioGroup';
import FormControlLabel from '@material-ui/core/FormControlLabel';
import FormControl from '@material-ui/core/FormControl';
import FormLabel from '@material-ui/core/FormLabel';
import LinearProgress from '@material-ui/core/LinearProgress';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogContentText from '@material-ui/core/DialogContentText';
import PublishIcon from '@material-ui/icons/Publish';

import { 
  useRefresh, 
  useTranslate,
  Button as RAButton,
} from 'react-admin';

import { sgxFetchApiWithToken, formDataPayload } from '../api/fetch';

import HttpErrorAlert from './HttpErrorAlert';

const importerFetch = (resource, options) => sgxFetchApiWithToken(`backoffice/${resource}/importer`, options);

const ImportCSVDialog = ({parserOptions, resource, open, onClose, record}) => {

  const [values, setValues] = useState({file: {}, format: parserOptions[0]});
  const [attaching, setAttaching] = useState(false);
  const [error, setError] = useState();

  const translate = useTranslate();

  const handleSubmit = () => {
    setAttaching(true);
    importerFetch(resource, formDataPayload('POST', {entity: record, ...values}))
    .then( data => handleClose())
    .catch(error => {
      setValues({...values, file: {}});
      setAttaching(false);
      setError(error);
    });
  };

  const handleChange = (event) => {
    setValues({...values, format: event.target.value});
  };

  const selectFile = ({target}) => {
    const file = target?.files[0];
    setValues({...values, file: {rawFile: file, title: file?.name}});
    setError();
  };

  const handleClose = () => {
    setValues({...values, file: {}});
    setAttaching(false);
    setError();
    onClose();
  };

  return (
  <Dialog open={open} onClose={handleClose} aria-labelledby="form-dialog-title">
    <DialogTitle id="form-dialog-title">Importar información</DialogTitle>
      <DialogContent>
        <DialogContentText>
          Adjunte una planilla con los valores a importar
        </DialogContentText>
        <div>
        <FormControl>
          <Button disabled={attaching} variant="contained" component="label" >
          {values.file.title || 'Buscar'}
          <input type="file" accept="text/csv" hidden onChange={selectFile} />
        </Button>
        </FormControl>
        </div>
        <div>
          <p>&nbsp;</p>
        </div>
        <div>
        <FormControl>
          <FormLabel component="legend">Formato</FormLabel>
          <RadioGroup aria-label="format" name="format" value={values.format} onChange={handleChange}>
            { parserOptions.map((id) => <FormControlLabel key={`option-${id}`} value={id} control={<Radio />} label={translate(`resources.${resource}.import.${id}`)} />)}
          </RadioGroup>
        </FormControl>
        </div>
      </DialogContent>
      {attaching && <LinearProgress /> }
      {error && <HttpErrorAlert error={error}/>}
      <DialogActions>
        <Button disabled={attaching} onClick={handleClose} color="primary">
          Cancelar
        </Button>
        <Button disabled={!values.file.rawFile || attaching} onClick={handleSubmit} color="primary">
          Importar
        </Button>
      </DialogActions>
  </Dialog>
  );
};

const ImportButton = ({resource, record}) => {
  const [open, setOpen] = useState(false);
  const [options, setOptions] = useState([]);
  const [loading, setLoading] = useState(false);

  const refresh = useRefresh();

  useEffect(() => {
    setLoading(true);
    importerFetch(resource)
        .then((result) => {
            if (result) {
              setOptions(result);
              setLoading(false);
            }
        })
        .catch((error = {}) => {
            setLoading(false);
        })
}, [resource]);

  const handleClickOpen = () => {
    setOpen(true);
  };

  const handleClose = () => {
    setOpen(false);
    refresh();
  };

  return (
    <div>
      <RAButton
        onClick={handleClickOpen}
        label="Importar"
        disabled={loading}
      >
        <PublishIcon />
      </RAButton>
      {!!options?.length && <ImportCSVDialog open={open} onClose={handleClose} parserOptions={options} resource={resource} record={record}/>}
      
    </div>
  );
};

export default ImportButton;