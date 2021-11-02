package ar.lamansys.odontology.application.createConsultation;

import ar.lamansys.odontology.application.odontogram.GetToothService;
import ar.lamansys.odontology.domain.ESurfacePositionBo;
import ar.lamansys.odontology.domain.OdontologySnomedBo;
import ar.lamansys.odontology.domain.ToothBo;
import ar.lamansys.odontology.domain.consultation.ConsultationBo;
import ar.lamansys.odontology.domain.consultation.ConsultationCpoCeoIndicesStorage;
import ar.lamansys.odontology.domain.consultation.ConsultationDentalActionBo;
import ar.lamansys.odontology.domain.consultation.CpoCeoIndicesBo;
import ar.lamansys.odontology.domain.consultation.OdontologyConsultationStorage;
import ar.lamansys.odontology.domain.consultation.ToothIndicesStorage;
import ar.lamansys.odontology.domain.consultation.cpoCeoIndices.ECeoIndexBo;
import ar.lamansys.odontology.domain.consultation.cpoCeoIndices.ECpoIndexBo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
class CpoCeoIndicesCalculatorTest {

    private CpoCeoIndicesCalculator cpoCeoIndicesCalculator;

    @Mock
    private GetToothService getToothService;

    @Mock
    private ConsultationCpoCeoIndicesStorage consultationCpoCeoIndicesStorage;

    @Mock
    private OdontologyConsultationStorage odontologyConsultationStorage;

    @Mock
    private ToothIndicesStorage toothIndicesStorage;

    @BeforeEach
    void setUp() {
        cpoCeoIndicesCalculator = new CpoCeoIndicesCalculator(
                getToothService,
                consultationCpoCeoIndicesStorage,
                odontologyConsultationStorage,
                toothIndicesStorage);
    }

    @Test
    void shouldSaveIndicesTwiceWhenPatientHasNoPreviousConsultations() {
        Integer patientId = 1;
        ConsultationBo consultationBo = new ConsultationBo();
        consultationBo.setPatientId(patientId);
        when(odontologyConsultationStorage.hasPreviousConsultations(patientId)).thenReturn(false);

        cpoCeoIndicesCalculator.run(consultationBo);

        ArgumentCaptor<CpoCeoIndicesBo> indicesCaptor = ArgumentCaptor.forClass(CpoCeoIndicesBo.class);
        ArgumentCaptor<Integer> patientIdCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(consultationCpoCeoIndicesStorage, times(2)).saveIndices(patientIdCaptor.capture(), indicesCaptor.capture());
    }

    @Test
    void allIndicesShouldBeZeroWhenThereAreNoDentalActions() {
        Integer patientId = 1;
        ConsultationBo consultationBo = new ConsultationBo();
        consultationBo.setPatientId(patientId);
        when(odontologyConsultationStorage.hasPreviousConsultations(patientId)).thenReturn(true);

        cpoCeoIndicesCalculator.run(consultationBo);

        ArgumentCaptor<CpoCeoIndicesBo> indicesCaptor = ArgumentCaptor.forClass(CpoCeoIndicesBo.class);
        ArgumentCaptor<Integer> patientIdCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(consultationCpoCeoIndicesStorage, times(1)).saveIndices(patientIdCaptor.capture(), indicesCaptor.capture());

        Assertions.assertEquals(0, indicesCaptor.getValue().getPermanentC());
        Assertions.assertEquals(0, indicesCaptor.getValue().getPermanentP());
        Assertions.assertEquals(0, indicesCaptor.getValue().getPermanentO());
        Assertions.assertEquals(0, indicesCaptor.getValue().getTemporaryC());
        Assertions.assertEquals(0, indicesCaptor.getValue().getTemporaryE());
        Assertions.assertEquals(0, indicesCaptor.getValue().getTemporaryO());
    }

    @Test
    void permanentOShouldBe1WhenSurfaceCavitiesAreFixedInTheWholeTooth() {
        Integer patientId = 1;
        ConsultationBo consultationBo = new ConsultationBo();
        consultationBo.setPatientId(patientId);
        when(odontologyConsultationStorage.hasPreviousConsultations(patientId)).thenReturn(true);
        ToothBo tooth = new ToothBo(new OdontologySnomedBo("TOOTH 1","TOOTH 1"), (short) 1,(short) 1, true);
        when(getToothService.run(any())).thenReturn(tooth);

        List<ConsultationDentalActionBo> dentalActions = new ArrayList<>();
        addSurfaceAction(dentalActions, "TOOTH 1", "CAVITIES", ESurfacePositionBo.CENTRAL, ECpoIndexBo.C);
        addSurfaceAction(dentalActions, "TOOTH 1", "CAVITIES", ESurfacePositionBo.EXTERNAL, ECpoIndexBo.C);
        addWholeToothActionInPermanentTooth(dentalActions, "TOOTH 1", "FILLING", ECpoIndexBo.O);
        consultationBo.setDentalActions(dentalActions);

        cpoCeoIndicesCalculator.run(consultationBo);

        ArgumentCaptor<CpoCeoIndicesBo> indicesCaptor = ArgumentCaptor.forClass(CpoCeoIndicesBo.class);
        ArgumentCaptor<Integer> patientIdCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(consultationCpoCeoIndicesStorage, times(1)).saveIndices(patientIdCaptor.capture(), indicesCaptor.capture());

        Assertions.assertEquals(0, indicesCaptor.getValue().getPermanentC());
        Assertions.assertEquals(0, indicesCaptor.getValue().getPermanentP());
        Assertions.assertEquals(1, indicesCaptor.getValue().getPermanentO());
        Assertions.assertEquals(0, indicesCaptor.getValue().getTemporaryC());
        Assertions.assertEquals(0, indicesCaptor.getValue().getTemporaryE());
        Assertions.assertEquals(0, indicesCaptor.getValue().getTemporaryO());
    }

    @Test
    void cavitiesInSurfacesAndWholeToothShouldCountAsOneCIndex() {
        Integer patientId = 1;
        ConsultationBo consultationBo = new ConsultationBo();
        consultationBo.setPatientId(patientId);
        when(odontologyConsultationStorage.hasPreviousConsultations(patientId)).thenReturn(true);
        ToothBo tooth = new ToothBo(new OdontologySnomedBo("TOOTH 1","TOOTH 1"), (short) 1,(short) 1, true);
        when(getToothService.run(any())).thenReturn(tooth);

        List<ConsultationDentalActionBo> dentalActions = new ArrayList<>();
        addSurfaceAction(dentalActions, "TOOTH 1", "CAVITIES", ESurfacePositionBo.CENTRAL, ECpoIndexBo.C);
        addSurfaceAction(dentalActions, "TOOTH 1", "CAVITIES", ESurfacePositionBo.EXTERNAL, ECpoIndexBo.C);
        addWholeToothActionInPermanentTooth(dentalActions, "TOOTH 1", "CAVITIES", ECpoIndexBo.C);
        consultationBo.setDentalActions(dentalActions);

        cpoCeoIndicesCalculator.run(consultationBo);

        ArgumentCaptor<CpoCeoIndicesBo> indicesCaptor = ArgumentCaptor.forClass(CpoCeoIndicesBo.class);
        ArgumentCaptor<Integer> patientIdCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(consultationCpoCeoIndicesStorage, times(1)).saveIndices(patientIdCaptor.capture(), indicesCaptor.capture());

        Assertions.assertEquals(1, indicesCaptor.getValue().getPermanentC());
        Assertions.assertEquals(0, indicesCaptor.getValue().getPermanentP());
        Assertions.assertEquals(0, indicesCaptor.getValue().getPermanentO());
        Assertions.assertEquals(0, indicesCaptor.getValue().getTemporaryC());
        Assertions.assertEquals(0, indicesCaptor.getValue().getTemporaryE());
        Assertions.assertEquals(0, indicesCaptor.getValue().getTemporaryO());
    }

    @Test
    void actionsThatDontApplyToTheIndicesShouldNotModifyTheResult() {
        Integer patientId = 1;
        ConsultationBo consultationBo = new ConsultationBo();
        consultationBo.setPatientId(patientId);
        when(odontologyConsultationStorage.hasPreviousConsultations(patientId)).thenReturn(true);
        ToothBo tooth = new ToothBo(new OdontologySnomedBo("TOOTH 1","TOOTH 1"), (short) 1,(short) 1, true);
        when(getToothService.run(any())).thenReturn(tooth);

        List<ConsultationDentalActionBo> dentalActions = new ArrayList<>();
        addSurfaceAction(dentalActions, "TOOTH 1", "CAVITIES", ESurfacePositionBo.CENTRAL, ECpoIndexBo.C);
        addSurfaceAction(dentalActions, "TOOTH 1", "CAVITIES", ESurfacePositionBo.EXTERNAL, ECpoIndexBo.C);
        addWholeToothActionInPermanentTooth(dentalActions, "TOOTH 1", "OTHER ACTION", ECpoIndexBo.NONE);
        consultationBo.setDentalActions(dentalActions);

        cpoCeoIndicesCalculator.run(consultationBo);

        ArgumentCaptor<CpoCeoIndicesBo> indicesCaptor = ArgumentCaptor.forClass(CpoCeoIndicesBo.class);
        ArgumentCaptor<Integer> patientIdCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(consultationCpoCeoIndicesStorage, times(1)).saveIndices(patientIdCaptor.capture(), indicesCaptor.capture());

        Assertions.assertEquals(1, indicesCaptor.getValue().getPermanentC());
        Assertions.assertEquals(0, indicesCaptor.getValue().getPermanentP());
        Assertions.assertEquals(0, indicesCaptor.getValue().getPermanentO());
        Assertions.assertEquals(0, indicesCaptor.getValue().getTemporaryC());
        Assertions.assertEquals(0, indicesCaptor.getValue().getTemporaryE());
        Assertions.assertEquals(0, indicesCaptor.getValue().getTemporaryO());
    }

    @Test
    void whenThereAreEqualNumberOfFixesAndCavitiesShouldCountAsOIndex() {
        Integer patientId = 1;
        ConsultationBo consultationBo = new ConsultationBo();
        consultationBo.setPatientId(patientId);
        when(odontologyConsultationStorage.hasPreviousConsultations(patientId)).thenReturn(true);
        ToothBo tooth = new ToothBo(new OdontologySnomedBo("TOOTH 1","TOOTH 1"), (short) 1,(short) 1, true);
        when(getToothService.run(any())).thenReturn(tooth);

        List<ConsultationDentalActionBo> dentalActions = new ArrayList<>();

        addSurfaceAction(dentalActions, "TOOTH 1", "CAVITIES", ESurfacePositionBo.CENTRAL, ECpoIndexBo.C);
        addSurfaceAction(dentalActions, "TOOTH 1", "CAVITIES", ESurfacePositionBo.EXTERNAL, ECpoIndexBo.C);
        addSurfaceAction(dentalActions, "TOOTH 1", "FILLING", ESurfacePositionBo.CENTRAL, ECpoIndexBo.O);
        addSurfaceAction(dentalActions, "TOOTH 1", "FILLING", ESurfacePositionBo.EXTERNAL, ECpoIndexBo.O);

        consultationBo.setDentalActions(dentalActions);

        cpoCeoIndicesCalculator.run(consultationBo);

        ArgumentCaptor<CpoCeoIndicesBo> indicesCaptor = ArgumentCaptor.forClass(CpoCeoIndicesBo.class);
        ArgumentCaptor<Integer> patientIdCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(consultationCpoCeoIndicesStorage, times(1)).saveIndices(patientIdCaptor.capture(), indicesCaptor.capture());

        Assertions.assertEquals(0, indicesCaptor.getValue().getPermanentC());
        Assertions.assertEquals(0, indicesCaptor.getValue().getPermanentP());
        Assertions.assertEquals(1, indicesCaptor.getValue().getPermanentO());
        Assertions.assertEquals(0, indicesCaptor.getValue().getTemporaryC());
        Assertions.assertEquals(0, indicesCaptor.getValue().getTemporaryE());
        Assertions.assertEquals(0, indicesCaptor.getValue().getTemporaryO());
    }

    @Test
    void lostToothActionShouldRemainAfterWhateverComesAfter() {
        Integer patientId = 1;
        ConsultationBo consultationBo = new ConsultationBo();
        consultationBo.setPatientId(patientId);
        when(odontologyConsultationStorage.hasPreviousConsultations(patientId)).thenReturn(true);
        ToothBo tooth = new ToothBo(new OdontologySnomedBo("TOOTH 1","TOOTH 1"), (short) 1,(short) 1, true);
        when(getToothService.run(any())).thenReturn(tooth);

        List<ConsultationDentalActionBo> dentalActions = new ArrayList<>();
        addWholeToothActionInPermanentTooth(dentalActions, "TOOTH 1", "LOST TOOTH", ECpoIndexBo.P);
        addSurfaceAction(dentalActions, "TOOTH 1", "CAVITIES", ESurfacePositionBo.EXTERNAL, ECpoIndexBo.C);
        addSurfaceAction(dentalActions, "TOOTH 1", "FILLING", ESurfacePositionBo.CENTRAL, ECpoIndexBo.O);
        addSurfaceAction(dentalActions, "TOOTH 1", "OTHER ACTION", ESurfacePositionBo.EXTERNAL, ECpoIndexBo.NONE);
        consultationBo.setDentalActions(dentalActions);

        cpoCeoIndicesCalculator.run(consultationBo);

        ArgumentCaptor<CpoCeoIndicesBo> indicesCaptor = ArgumentCaptor.forClass(CpoCeoIndicesBo.class);
        ArgumentCaptor<Integer> patientIdCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(consultationCpoCeoIndicesStorage, times(1)).saveIndices(patientIdCaptor.capture(), indicesCaptor.capture());

        Assertions.assertEquals(0, indicesCaptor.getValue().getPermanentC());
        Assertions.assertEquals(1, indicesCaptor.getValue().getPermanentP());
        Assertions.assertEquals(0, indicesCaptor.getValue().getPermanentO());
        Assertions.assertEquals(0, indicesCaptor.getValue().getTemporaryC());
        Assertions.assertEquals(0, indicesCaptor.getValue().getTemporaryE());
        Assertions.assertEquals(0, indicesCaptor.getValue().getTemporaryO());
    }

    @Test
    void moreCavitiesInSurfacesThanFixesShouldResultInCIndex() {
        Integer patientId = 1;
        ConsultationBo consultationBo = new ConsultationBo();
        consultationBo.setPatientId(patientId);
        when(odontologyConsultationStorage.hasPreviousConsultations(patientId)).thenReturn(true);
        ToothBo tooth = new ToothBo(new OdontologySnomedBo("TOOTH 1","TOOTH 1"), (short) 1,(short) 1, true);
        when(getToothService.run(any())).thenReturn(tooth);

        List<ConsultationDentalActionBo> dentalActions = new ArrayList<>();
        addSurfaceAction(dentalActions, "TOOTH 1", "CAVITIES", ESurfacePositionBo.EXTERNAL, ECpoIndexBo.C);
        addSurfaceAction(dentalActions, "TOOTH 1", "CAVITIES", ESurfacePositionBo.CENTRAL, ECpoIndexBo.C);
        addSurfaceAction(dentalActions, "TOOTH 1", "CAVITIES", ESurfacePositionBo.INTERNAL, ECpoIndexBo.C);
        addSurfaceAction(dentalActions, "TOOTH 1", "FILLING", ESurfacePositionBo.LEFT, ECpoIndexBo.O);
        consultationBo.setDentalActions(dentalActions);

        cpoCeoIndicesCalculator.run(consultationBo);

        ArgumentCaptor<CpoCeoIndicesBo> indicesCaptor = ArgumentCaptor.forClass(CpoCeoIndicesBo.class);
        ArgumentCaptor<Integer> patientIdCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(consultationCpoCeoIndicesStorage, times(1)).saveIndices(patientIdCaptor.capture(), indicesCaptor.capture());

        Assertions.assertEquals(1, indicesCaptor.getValue().getPermanentC());
        Assertions.assertEquals(0, indicesCaptor.getValue().getPermanentP());
        Assertions.assertEquals(0, indicesCaptor.getValue().getPermanentO());
        Assertions.assertEquals(0, indicesCaptor.getValue().getTemporaryC());
        Assertions.assertEquals(0, indicesCaptor.getValue().getTemporaryE());
        Assertions.assertEquals(0, indicesCaptor.getValue().getTemporaryO());
    }

    @Test
    void cavitiesAfterFillingAfterCavitiesShouldResultInCIndex() {
        Integer patientId = 1;
        ConsultationBo consultationBo = new ConsultationBo();
        consultationBo.setPatientId(patientId);
        when(odontologyConsultationStorage.hasPreviousConsultations(patientId)).thenReturn(true);
        ToothBo tooth = new ToothBo(new OdontologySnomedBo("TOOTH 1","TOOTH 1"), (short) 1,(short) 1, true);
        when(getToothService.run(any())).thenReturn(tooth);

        List<ConsultationDentalActionBo> dentalActions = new ArrayList<>();
        addWholeToothActionInPermanentTooth(dentalActions, "TOOTH 1", "CAVITIES", ECpoIndexBo.C);
        addWholeToothActionInPermanentTooth(dentalActions, "TOOTH 1", "FILLING", ECpoIndexBo.O);
        addWholeToothActionInPermanentTooth(dentalActions, "TOOTH 1", "CAVITIES", ECpoIndexBo.C);
        consultationBo.setDentalActions(dentalActions);

        cpoCeoIndicesCalculator.run(consultationBo);

        ArgumentCaptor<CpoCeoIndicesBo> indicesCaptor = ArgumentCaptor.forClass(CpoCeoIndicesBo.class);
        ArgumentCaptor<Integer> patientIdCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(consultationCpoCeoIndicesStorage, times(1)).saveIndices(patientIdCaptor.capture(), indicesCaptor.capture());

        Assertions.assertEquals(1, indicesCaptor.getValue().getPermanentC());
        Assertions.assertEquals(0, indicesCaptor.getValue().getPermanentP());
        Assertions.assertEquals(0, indicesCaptor.getValue().getPermanentO());
        Assertions.assertEquals(0, indicesCaptor.getValue().getTemporaryC());
        Assertions.assertEquals(0, indicesCaptor.getValue().getTemporaryE());
        Assertions.assertEquals(0, indicesCaptor.getValue().getTemporaryO());
    }

    @Test
    void actionsAppliedToTemporaryTeethShouldNotInterfereWithTheOthers() {
        Integer patientId = 1;
        ConsultationBo consultationBo = new ConsultationBo();
        consultationBo.setPatientId(patientId);
        when(odontologyConsultationStorage.hasPreviousConsultations(patientId)).thenReturn(true);
        ToothBo tooth1 = new ToothBo(new OdontologySnomedBo("TOOTH 1","TOOTH 1"), (short) 1,(short) 1, true);
        ToothBo tooth2 = new ToothBo(new OdontologySnomedBo("TOOTH 2","TOOTH 2"), (short) 1,(short) 6, false);
        when(getToothService.run("TOOTH 1")).thenReturn(tooth1);
        when(getToothService.run("TOOTH 2")).thenReturn(tooth2);

        List<ConsultationDentalActionBo> dentalActions = new ArrayList<>();
        addWholeToothActionInPermanentTooth(dentalActions, "TOOTH 1", "CAVITIES", ECpoIndexBo.C);
        addWholeToothActionInPermanentTooth(dentalActions, "TOOTH 1", "FILLING", ECpoIndexBo.O);
        addWholeToothActionInPermanentTooth(dentalActions, "TOOTH 1", "CAVITIES", ECpoIndexBo.C);

        addWholeToothActionInTemporaryTooth(dentalActions, "TOOTH 2", "CAVITIES", ECeoIndexBo.C);
        addWholeToothActionInTemporaryTooth(dentalActions, "TOOTH 2", "FILLING", ECeoIndexBo.O);
        addWholeToothActionInTemporaryTooth(dentalActions, "TOOTH 2", "CAVITIES", ECeoIndexBo.C);
        consultationBo.setDentalActions(dentalActions);

        cpoCeoIndicesCalculator.run(consultationBo);

        ArgumentCaptor<CpoCeoIndicesBo> indicesCaptor = ArgumentCaptor.forClass(CpoCeoIndicesBo.class);
        ArgumentCaptor<Integer> patientIdCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(consultationCpoCeoIndicesStorage, times(1)).saveIndices(patientIdCaptor.capture(), indicesCaptor.capture());

        Assertions.assertEquals(1, indicesCaptor.getValue().getPermanentC());
        Assertions.assertEquals(0, indicesCaptor.getValue().getPermanentP());
        Assertions.assertEquals(0, indicesCaptor.getValue().getPermanentO());
        Assertions.assertEquals(1, indicesCaptor.getValue().getTemporaryC());
        Assertions.assertEquals(0, indicesCaptor.getValue().getTemporaryE());
        Assertions.assertEquals(0, indicesCaptor.getValue().getTemporaryO());
    }

    @Test
    void actionsAppliedToDifferentPermanentTeethShouldAddToTheCPOResult() {
        Integer patientId = 1;
        ConsultationBo consultationBo = new ConsultationBo();
        consultationBo.setPatientId(patientId);
        when(odontologyConsultationStorage.hasPreviousConsultations(patientId)).thenReturn(true);
        ToothBo tooth1 = new ToothBo(new OdontologySnomedBo("TOOTH 1","TOOTH 1"), (short) 1,(short) 1, true);
        ToothBo tooth2 = new ToothBo(new OdontologySnomedBo("TOOTH 2","TOOTH 2"), (short) 2,(short) 1, true);
        ToothBo tooth3 = new ToothBo(new OdontologySnomedBo("TOOTH 3","TOOTH 3"), (short) 3,(short) 1, true);
        ToothBo tooth4 = new ToothBo(new OdontologySnomedBo("TOOTH 4","TOOTH 4"), (short) 4,(short) 1, true);
        ToothBo tooth5 = new ToothBo(new OdontologySnomedBo("TOOTH 5","TOOTH 5"), (short) 5,(short) 1, true);
        when(getToothService.run("TOOTH 1")).thenReturn(tooth1);
        when(getToothService.run("TOOTH 2")).thenReturn(tooth2);
        when(getToothService.run("TOOTH 3")).thenReturn(tooth3);
        when(getToothService.run("TOOTH 4")).thenReturn(tooth4);
        when(getToothService.run("TOOTH 5")).thenReturn(tooth5);

        List<ConsultationDentalActionBo> dentalActions = new ArrayList<>();
        addWholeToothActionInPermanentTooth(dentalActions, "TOOTH 1", "CAVITIES", ECpoIndexBo.C);
        addWholeToothActionInPermanentTooth(dentalActions, "TOOTH 1", "FILLING", ECpoIndexBo.O);
        addWholeToothActionInPermanentTooth(dentalActions, "TOOTH 1", "CAVITIES", ECpoIndexBo.C);

        addWholeToothActionInPermanentTooth(dentalActions, "TOOTH 2", "CAVITIES", ECpoIndexBo.C);
        addWholeToothActionInPermanentTooth(dentalActions, "TOOTH 2", "OTHER ACTION", ECpoIndexBo.NONE);

        addWholeToothActionInPermanentTooth(dentalActions, "TOOTH 3", "LOST", ECpoIndexBo.P);

        addWholeToothActionInPermanentTooth(dentalActions, "TOOTH 4", "LOST", ECpoIndexBo.P);

        addWholeToothActionInPermanentTooth(dentalActions, "TOOTH 5", "CAVITIES", ECpoIndexBo.C);
        addWholeToothActionInPermanentTooth(dentalActions, "TOOTH 5", "FILLING", ECpoIndexBo.O);
        consultationBo.setDentalActions(dentalActions);

        cpoCeoIndicesCalculator.run(consultationBo);

        ArgumentCaptor<CpoCeoIndicesBo> indicesCaptor = ArgumentCaptor.forClass(CpoCeoIndicesBo.class);
        ArgumentCaptor<Integer> patientIdCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(consultationCpoCeoIndicesStorage, times(1)).saveIndices(patientIdCaptor.capture(), indicesCaptor.capture());

        Assertions.assertEquals(2, indicesCaptor.getValue().getPermanentC());
        Assertions.assertEquals(2, indicesCaptor.getValue().getPermanentP());
        Assertions.assertEquals(1, indicesCaptor.getValue().getPermanentO());
        Assertions.assertEquals(0, indicesCaptor.getValue().getTemporaryC());
        Assertions.assertEquals(0, indicesCaptor.getValue().getTemporaryE());
        Assertions.assertEquals(0, indicesCaptor.getValue().getTemporaryO());
    }

    private void addWholeToothActionInPermanentTooth(List<ConsultationDentalActionBo> dentalActions, String toothName,
                                                     String actionName, ECpoIndexBo index) {
        ConsultationDentalActionBo action = new ConsultationDentalActionBo();
        action.setSnomed(new OdontologySnomedBo(actionName, actionName));
        action.setTooth(new OdontologySnomedBo(toothName, toothName));
        action.setPermanentIndex(index);
        action.setAppliedToTemporaryTooth(false);
        dentalActions.add(action);
    }

    private void addWholeToothActionInTemporaryTooth(List<ConsultationDentalActionBo> dentalActions, String toothName,
                                                     String actionName, ECeoIndexBo index) {
        ConsultationDentalActionBo action = new ConsultationDentalActionBo();
        action.setSnomed(new OdontologySnomedBo(actionName, actionName));
        action.setTooth(new OdontologySnomedBo(toothName, toothName));
        action.setTemporaryIndex(index);
        action.setAppliedToTemporaryTooth(true);
        dentalActions.add(action);
    }

    private void addSurfaceAction(List<ConsultationDentalActionBo> dentalActions, String toothName, String actionName,
                                  ESurfacePositionBo position, ECpoIndexBo index) {
        ConsultationDentalActionBo action = new ConsultationDentalActionBo();
        action.setSnomed(new OdontologySnomedBo(actionName, actionName));
        action.setTooth(new OdontologySnomedBo(toothName, toothName));
        action.setSurfacePosition(position);
        action.setPermanentIndex(index);
        action.setAppliedToTemporaryTooth(false);
        dentalActions.add(action);
    }

}