package de.thowl.notetilus.core.services;

import de.thowl.notetilus.storage.entities.AccessToken;

public interface AuthenticationService {
    
    /**
     * Performs an "login" action for demonstrition purpuses.
     * No Accestokens or similar this are handled
     * 
     * Source: https://www.masterspringboot.com/security/authentication/using-bcryptpasswordencoder-to-encrypt-your-passwords/
     * 
     * @param email
     * @param password
     * @return Username of user
     * @return @Code{}
     */
    // TODO: improve Javadoc comment
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
