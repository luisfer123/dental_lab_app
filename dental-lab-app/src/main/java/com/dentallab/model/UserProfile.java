package com.dentallab.model;

public class UserProfile {
	
	private Long id;
    private String username;
    private String email;
    private String role;

    /**
     * Role-specific details (Dentist, Technician, or Patient)
     * stored in a single polymorphic variable.
     */
    private AbstractRoleProfile profile;

}
