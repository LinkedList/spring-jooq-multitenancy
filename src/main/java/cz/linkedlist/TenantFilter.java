package cz.linkedlist;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Martin Macko <https://github.com/LinkedList>.
 */
@Component
public class TenantFilter implements Filter {

    private static final String TENANT_KEY = "tenant";
    private static final String CURRENT_TENANT_IDENTIFIER = "CURRENT_TENANT_IDENTIFIER";

    private final TenantContext context;
    private final JdbcTemplate jdbc;

    public TenantFilter(TenantContext context, JdbcTemplate jdbc) {
        this.context = context;
        this.jdbc = jdbc;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        if (req.getHeader(TENANT_KEY) != null) {
            req.setAttribute(CURRENT_TENANT_IDENTIFIER, req.getHeader(TENANT_KEY));
            context.setTenant(getTenant(req.getHeader(TENANT_KEY)));
        } else {
            throw new RuntimeException("No tenant set you dumbass!!!");
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
    }

    private Tenant getTenant(String tenant) {
        return jdbc.queryForObject(
                "select * from tenants.tenants where schema = (?)",
                new Object[]{tenant},
                new BeanPropertyRowMapper<>(Tenant.class));
    }
}
