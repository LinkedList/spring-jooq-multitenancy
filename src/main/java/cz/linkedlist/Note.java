package cz.linkedlist;

import lombok.Data;

/**
 * @author Martin Macko <https://github.com/LinkedList>.
 */
@Data
public class Note {
    private Integer id;
    private String note;
    private NoteType type;
}
