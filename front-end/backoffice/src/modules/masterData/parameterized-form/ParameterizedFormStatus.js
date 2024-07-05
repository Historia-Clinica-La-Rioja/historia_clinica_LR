export const FORM_STATUS_CHOICES = [
    { id: 1, name: 'resources.parameterizedform.statusId.draft'},
    { id: 2, name: 'resources.parameterizedform.statusId.active'},
    { id: 3, name: 'resources.parameterizedform.statusId.inactive'}
]

export const formIsUpdatable = (statusId) => {
    return statusId && statusId === 1;
}

export const FORM_STATUS_ACTIONS = {
    1: {nextStateActionName: 'resources.parameterizedform.statusId.activate'},
    2: {nextStateActionName: 'resources.parameterizedform.statusId.deactivate'},
    3: {nextStateActionName: 'resources.parameterizedform.statusId.activate'}
}