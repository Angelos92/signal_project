package com.data_management;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * Handles access-control rules for patient data.
 * Only authorized roles should be allowed to retrieve sensitive patient records.
 */
public class AccessController {
    private Set<String> allowedRoles;

    /**
     * Constructs an AccessController with default authorized roles.
     */
    public AccessController() {
        this.allowedRoles = new HashSet<>(Arrays.asList("Doctor", "Nurse", "Admin"));
    }

    /**
     * Checks whether a requester role is allowed to access patient data.
     *
     * @param requesterRole the role of the person requesting access
     * @return true if the role is authorized, false otherwise
     */
    public boolean hasAccess(String requesterRole) {
        if (requesterRole == null) {
            return false;
        }

        for (String role : allowedRoles) {
            if (role.equalsIgnoreCase(requesterRole)) {
                return true;
            }
        }

        return false;
    }
}