import InstitutionPracticesList from './InstitutionPracticesList';
import InstitutionPracticesCreate from "./InstitutionPracticesCreate";
import InstitutionPracticesShow from "./InstitutionPracticesShow";
import InstitutionPracticesEdit from "./InstitutionPracticesEdit";

const institutionpractices = {
    list: InstitutionPracticesList,
    show: InstitutionPracticesShow,
    create: InstitutionPracticesCreate,
    edit: InstitutionPracticesEdit,
    options: {
        submenu: 'more'
    }
};

export default institutionpractices;
