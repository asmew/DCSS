import net.jini.core.entry.Entry;

/*
A basic class to live in the java space that allows any new instances of the
client to pull a new, incremented item ID number.
Doing this in the space allows any different numbers of the program to access
a new unique item ID to avoid multiple items getting the same item ID.
*/
public class SpaceCounter implements Entry {

    //Stored Integer of the current item ID count

    public Integer itemCount;

    public SpaceCounter(){}


    //Used to set the first value to begin counting, initialised upon execution of the main class to 0
    public SpaceCounter(int itemCount) {
        this.itemCount = itemCount;
    }

    //Getter for the current itemCount
    public Integer getNumber(){
        return itemCount;
    }

    //The method called when a new lot needs a new unique item number. Returns the new value after incrementing
    public Integer iterate(){
        return ++itemCount;
    }
}
