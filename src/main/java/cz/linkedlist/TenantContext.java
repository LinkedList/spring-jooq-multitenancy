package cz.linkedlist;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import java.io.Serializable;

/**
 * @author Martin Macko <https://github.com/LinkedList>.
 */
@SessionScope
@Data
@Component
public class TenantContext implements Serializable {

    Tenant tenant;

}
