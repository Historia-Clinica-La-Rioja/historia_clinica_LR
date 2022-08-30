import MandatoryMedicalPracticesShow from "./show";
import MandatoryMedicalPracticesList from "./list";
import MandatoryMedicalPracticeCreate from "./create";
import MandatoryMedicalPracticeEdit from "./edit";


const mandatorymedicalpractices = {
    show: MandatoryMedicalPracticesShow,
    list: MandatoryMedicalPracticesList,
    create: MandatoryMedicalPracticeCreate,
    edit: MandatoryMedicalPracticeEdit,
    options: {
        submenu: 'booking'
    }
};

export default mandatorymedicalpractices;
