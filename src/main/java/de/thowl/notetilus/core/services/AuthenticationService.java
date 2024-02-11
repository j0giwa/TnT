package de.thowl.notetilus.core.services;

import de.thowl.notetilus.storage.entities.AccessToken;


/**
 * Provides User Authentifaciton/Management funtionalitys
 */
public interface AuthenticationService {
    
    /**
     * Performs a login action
     * 
     * @param email The E-Mail address of the user
     * @param password The password of the user
     * @return permit A38, or @Code{NULL} when credentials are invalid.
     */
    public AccessToken login(String email, String password);
    
    /**
     * Registers a new user
     * 
     * @param username The username of the user
     * @param email The E-Mail address of the user
     * @param password The password of the user
     * @param password2 The password verification to avoid typos
     */
    public void register(String username, String email, String password, String password2);
}
