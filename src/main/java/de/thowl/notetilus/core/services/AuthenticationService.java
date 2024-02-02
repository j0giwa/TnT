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
    public AccessToken login(String email, String password);
}
