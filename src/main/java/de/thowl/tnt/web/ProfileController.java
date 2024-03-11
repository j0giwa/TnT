package de.thowl.tnt.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class ProfileController {

    /**
	 * Shows the profile page
	 * 
	 * @return profile.html
	 */
    @RequestMapping(value = "/u/{username}/profile", method = RequestMethod.GET)
    public String showProfilePage() {
        log.info("entering showProfilePage (GET-Method: /profile)");
        return "profile";
    }
}
