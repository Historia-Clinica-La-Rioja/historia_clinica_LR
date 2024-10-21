package ar.lamansys.sgh.clinichistory.infrastructure.input.port;

import ar.lamansys.sgh.clinichistory.application.notes.NoteService;

import ar.lamansys.sgh.shared.infrastructure.input.service.SharedNotePort;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class NotePortImpl implements SharedNotePort {

	private final NoteService noteService;

	@Override
	public Long saveNote(String note) {
		return noteService.createNote(note);
	}
}
