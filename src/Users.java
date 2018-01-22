/*
A class to store the ID of the user to be accessed.
The value should never change throughout a single execution of the program.
 */

public final class Users {

    /*
    The current user ID stored here
     */

    private static String user;

    public static String getUser(){
        return user;
    }

    public static String setUser(String idIn){
        user = idIn;
        System.out.println("Current Registered User is: " + idIn);
        return user;
    }





}
