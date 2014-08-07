package fr.ropiteaux.rom.core.security;

import java.util.UUID;

import com.google.inject.Inject;

import fr.ropiteaux.rom.core.model.User;
import fr.ropiteaux.rom.core.services.UserService;

/**
 * Created by brunetj on 03/06/2014.
 */
public class RomSecurity {

    @Inject
    private UserService userService;

    public RomSecurity() {
    }

    public User validateAuthentication(String userId, String requestAuthToken) throws RomSecurityException {
        User user = userService.findById(new Long(userId));

        if(user!=null) {
            if(user.getAuthToken()!=null && !user.getAuthToken().equals("")) {
                try {
                    UUID requestAuthTokenUUID = UUID.fromString(requestAuthToken);
                    UUID userAuthTokenUUID = UUID.fromString(user.getAuthToken());
                    if (requestAuthTokenUUID.compareTo(userAuthTokenUUID) != 0) {
                        throw new RomSecurityException(RomSecurityException.INVALID_USER_TOKEN);
                    } else return user; //authentication validated
                } catch(Exception e) {
                    throw new RomSecurityException(RomSecurityException.INVALID_USER_TOKEN);
                }
            } else throw new RomSecurityException(RomSecurityException.INVALID_USER_TOKEN);

        } else throw new RomSecurityException(RomSecurityException.UNKNWON_USER);

    }

    public boolean validateAuthorization(String userId, String requestURI) {
        return true; //TODO
    }

    public User authenticate(String username, String password) {

        User user = userService.findByEmail(username);

        if(user!=null && user.getPassword().equals(password)) {//at this point, pwd is md5 hashed
            String authToken = UUID.randomUUID().toString();
            user.setAuthToken(authToken);
            userService.update(user);
            return user;
        }
        else
            return null;
    }

    public void logout (Long userId) throws RomSecurityException {
        User user = userService.findById(userId);

        if(user!=null) {
            user.setAuthToken(null);
            userService.update(user);
        }
        else throw new RomSecurityException(RomSecurityException.UNKNWON_USER);
    }


}