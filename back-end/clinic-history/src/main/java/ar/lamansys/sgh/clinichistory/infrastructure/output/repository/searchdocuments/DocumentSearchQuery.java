package ar.lamansys.sgh.clinichistory.infrastructure.output.repository.searchdocuments;

import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.DocumentStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ConditionVerificationStatus;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.masterdata.entity.ProblemType;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.document.SourceType;
import ar.lamansys.sgx.shared.repositories.QueryPart;
import ar.lamansys.sgx.shared.repositories.QueryStringHelper;
import com.google.common.base.Strings;
import lombok.NoArgsConstructor;
import ar.lamansys.sgh.clinichistory.infrastructure.output.repository.hospitalizationState.entity.DocumentObservationsVo;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@NoArgsConstructor
public class DocumentSearchQuery {

    String plainText;
    String escapeSqlText;
    private static final String MESSAGE = "No se han creado documentos aún";

    public DocumentSearchQuery(String plainText){
        this.plainText = plainText;
        this.escapeSqlText = QueryStringHelper.escapeSql(plainText);
    }

    public QueryPart select() {
        return new QueryPart("document.id, \n" +
                "document.creationable.createdOn, \n" +
				"userperson.pk.userId as creatorUserId, \n" +
				"creator.firstName, \n" +
                "creator.lastName, \n" +
                "snomed.pt as diagnosis, \n" +
                "hc.main, \n" +
				"hc.problemId, \n" +
				"hc.verificationStatusId, \n" +
				"documenttype.description , \n" +
                "othernote.description as otherNote, " +
                "physicalnote.description as physicalNote, \n" +
                "studiesnote.description as studiesNote, \n" +
                "evolutionnote.description as evolutionNote,\n" +
                "clinicalnote.description as clinicalNote, \n" +
                "illnessnote.description as illnessNote, \n" +
                "indicationnote.description as indicationNote, \n" +
				"personextended.nameSelfDetermination, \n" +
				"document.initialDocumentId as initialDocumentId, \n" +
				"document.statusId as statusId \n");
    }

    public QueryPart from() {
        return new QueryPart("Document as document \n" +
                "left join DocumentHealthCondition as dhc on (document.id = dhc.pk.documentId) \n" +
        //Creator
                "join UserPerson as userperson on (document.creationable.createdBy = userperson.pk.userId) \n" +
                "join Person as creator on (userperson.pk.personId = creator.id) \n" +
				"left join PersonExtended as personextended on (creator.id = personextended.id) \n" +
        //Notes
                "left join Note othernote on (document.otherNoteId = othernote.id) \n" +
                "left join Note physicalnote on (document.physicalExamNoteId = physicalnote.id) \n" +
                "left join Note studiesnote on (document.studiesSummaryNoteId = studiesnote.id) \n" +
                "left join Note evolutionnote on (document.evolutionNoteId = evolutionnote.id) \n" +
                "left join Note clinicalnote on (document.clinicalImpressionNoteId = clinicalnote.id) \n" +
                "left join Note illnessnote on (document.currentIllnessNoteId = illnessnote.id) \n" +
                "left join Note indicationnote on (document.indicationsNoteId = indicationnote.id) \n" +
         //Diagnosis
                "left join HealthCondition as hc on (dhc.pk.healthConditionId = hc.id ) \n" +
                "left join Snomed as snomed on (hc.snomedId = snomed.id ) \n" +
		//Document type
				"join DocumentType as documenttype on (document.typeId = documenttype.id) \n"
        );
    }

    public QueryPart where() {
        return new QueryPart("document.sourceId = :internmentEpisodeId \n" +
                "and document.sourceTypeId = " + SourceType.HOSPITALIZATION +" \n"+
				"and document.typeId != " + DocumentType.INDICATION +" \n"+
				"and not document.statusId = '" + DocumentStatus.ERROR +"' \n"+
				"and not exists (select 1 \n" +
				"					from HealthCondition hc2 \n" +
				"					where hc.id = hc2.id \n" +
				"					and (hc2.problemId = '" + ProblemType.HISTORY +"' \n"+
				"					or hc2.problemId = '" + ProblemType.PROBLEM +"' \n"+
				"					or hc2.verificationStatusId = '" + ConditionVerificationStatus.ERROR +"' \n))");
    }

    public QueryPart orderBy(){
        return new QueryPart("document.creationable.createdOn DESC ");
    }

    public String noResultMessage(){
        return MESSAGE;
    }

    public List<DocumentSearchVo> construct(List<Object[]> resultQuery){
        List<DocumentSearchVo> result = new ArrayList<>();

        Map<Long, List<Object[]>> diagnosisByDocuments = resultQuery
                .stream()
                .collect(Collectors.groupingBy(
                        (Object[] t) -> (Long)t[0],
                        LinkedHashMap::new,
                        toList())
                );
        diagnosisByDocuments.forEach((k,v) -> {
            Object[] tuple = v.get(0);
            result.add(new DocumentSearchVo(k,
                    mapNotes(tuple),
                    (LocalDateTime)tuple[1],
					(Integer)tuple[2],
                    (String)tuple[3],
                    (String)tuple[4],
                    mapDiagnosis(v),
                    mapMainDiagnosis(v),
					(String)tuple[9],
					(String)tuple[17],
					(Long) tuple[18],
					(String)tuple[19]));

        });
        return result;
    }

    private DocumentObservationsVo mapNotes(Object[] tuple){
        int index = 10;
        String otherNote = (String) tuple[index++];
        String physicalExam = (String)tuple[index++];
        String studiesSummary = (String)tuple[index++];
        String evolutionNote = (String)tuple[index++];
        String clinicalImpression = (String)tuple[index++];
        String currentIllness = (String)tuple[index++];
        String indicationsNote = (String)tuple[index];

        boolean withoutNotes = Strings.isNullOrEmpty(otherNote) &&
                Strings.isNullOrEmpty(physicalExam) &&
                Strings.isNullOrEmpty(studiesSummary) &&
                Strings.isNullOrEmpty(evolutionNote) &&
                Strings.isNullOrEmpty(clinicalImpression) &&
                Strings.isNullOrEmpty(currentIllness) &&
                Strings.isNullOrEmpty(indicationsNote);
        if(withoutNotes)
            return null;
        return new DocumentObservationsVo(otherNote, physicalExam, studiesSummary, evolutionNote,
                clinicalImpression, currentIllness, indicationsNote);
    }

    private String mapMainDiagnosis(List<Object[]> tuples){
        return tuples.stream()
                .filter((Object[]t) -> t[6] != null && (Boolean)t[6])
                .map((Object[]t) -> (String)t[5]).collect(Collectors.joining());
    }

    private List<String> mapDiagnosis(List<Object[]> tuples){
        return tuples.stream()
                .filter((Object[]t) -> t[6] != null && !(Boolean)t[6])
                .map((Object[]t) -> (String)t[5]).collect(Collectors.toList());
    }
}
