package myjava.myprojects.utils;

import org.mindrot.jbcrypt.BCrypt;

public class UserServiceUtil {

    public static String hashedPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

}
