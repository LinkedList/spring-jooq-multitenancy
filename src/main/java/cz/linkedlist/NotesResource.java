package cz.linkedlist;

import cz.linkedlist.data.master.tables.records.NotesRecord;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.jooq.Record;
import org.jooq.Result;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

import static cz.linkedlist.data.master.tables.Notes.NOTES;
import static cz.linkedlist.data.shared.tables.NoteType.NOTE_TYPE;

/**
 * @author Martin Macko <https://github.com/LinkedList>.
 */
@RestController
@Slf4j
public class NotesResource {

    private final TenantContext context;
    private final DSLContext create;
    private final ModelMapper mm;

    public NotesResource(TenantContext context, DSLContext create, ModelMapper mm) {
        this.context = context;
        this.create = create;
        this.mm = mm;
    }

    @GetMapping("/tenant")
    public ResponseEntity<String> get() {
        return new ResponseEntity<>(context.getTenant().toString(), HttpStatus.OK);
    }


    @GetMapping("/notes")
    public ResponseEntity<List<Note>> getNotes() {
        final Result<Record> records = create.select()
                .from(NOTES)
                .join(NOTE_TYPE).on(NOTES.NOTE_TYPE.eq(NOTE_TYPE.ID))
                .fetch();

        List<Note> notes = new ArrayList<>();
        for (Record record : records) {
            Note note = mm.map(record, Note.class);
            notes.add(note);
        }

        return new ResponseEntity<>(notes, HttpStatus.OK);
    }

    @PostMapping("/notes")
    public ResponseEntity<Note> createNote(@RequestBody Note note) {
        log.info("Creating a new note: {}", note);
        NotesRecord notesRecord = create.newRecord(NOTES, note);
        notesRecord.store();

        return new ResponseEntity<>(notesRecord.into(Note.class), HttpStatus.OK);
    }
}
