import ContactMailIcon from '@material-ui/icons/ContactMail';

import OrchestratorShow from './OrchestratorShow';
import OrchestratorList from './OrchestratorList';
import OrchestratorCreate from './OrchestratorCreate';
import OrchestratorEdit from './OrchestratorEdit';

const orchestrator = {
    icon: ContactMailIcon,
    show: OrchestratorShow,
    list: OrchestratorList,
    create: OrchestratorCreate,
    edit: OrchestratorEdit,
    options: {
        submenu: 'facilities'
    }
};

export default orchestrator;
