package ca.gatin.model.security;

/**
 * List of Roles.
 * Order and names has to match 'authority' table
 *
 * @author RGatin
 * @since Apr 17, 2016
 */
public enum Authorities {
    ROLE_USER,
    ROLE_ADMIN,
    ROLE_SUPERADMIN,
    ROLE_ANONYMOUS
}