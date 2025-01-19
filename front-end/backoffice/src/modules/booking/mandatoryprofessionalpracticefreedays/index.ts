
import MandatoryProfessionalPracticeFreeDaysList from './list';
import MandatoryProfessionalPracticeFreeDaysShow from './show';
import MandatoryProfessionalPracticeFreeDaysCreate from './create';

const mandatoryprofessionalpracticefreedays = {
    create: MandatoryProfessionalPracticeFreeDaysCreate,
    list: MandatoryProfessionalPracticeFreeDaysList,
    show: MandatoryProfessionalPracticeFreeDaysShow,
    options: {
        submenu: 'booking'
    }
};

export default mandatoryprofessionalpracticefreedays;
