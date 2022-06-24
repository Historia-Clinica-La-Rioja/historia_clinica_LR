import React from 'react';

import Card from '@material-ui/core/Card';
import CardContent from '@material-ui/core/CardContent';
import Typography from '@material-ui/core/Typography';
import LinearProgress from '@material-ui/core/LinearProgress';
import CardActions from '@material-ui/core/CardActions';
import Button from '@material-ui/core/Button';

import { Form } from 'react-final-form';


const FormCard = ({ 
    children, 
    title, 
    submitText, 
    onSubmit, 
    submitting, 
    validate,
}) => (
    <Form
      onSubmit={onSubmit}
      validate={validate}
      render={({ handleSubmit }) => (
        <form onSubmit={handleSubmit}>
          <Card style={{ margin: 8 }}>
            <CardContent>
              <Typography gutterBottom variant="h5" component="h2">
                {title}
              </Typography>
              {children.map((field, index) => <div key={`field-${index}`} style={{ margin: 8 }}>{field}</div>)}
            </CardContent>
            {submitting && <LinearProgress />}
            <CardActions>
              <Button color="primary" type="submit" disabled={submitting}>
                {submitText}
              </Button>
            </CardActions>
          </Card>
        </form>
      )}
    />
  );

export default FormCard;
