package cz.linkedlist;

import lombok.Data;

/**
 * @author Martin Macko <https://github.com/LinkedList>.
 */
@Data
public class Tenant {

    private int id;
    private String login;
    private String schema;
    private String password;

}
