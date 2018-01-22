import net.jini.core.entry.Entry;


/*
The class storing the object to be added to the space

We're going to be storing an object with an ID, Name, Description, current bid and user associated

 */

public class auctionObject implements Entry {

    public Integer id;
    public String user;
    public String name;
    public String description;
    public Double bid;
    public Boolean ended;
    public Boolean fixed;


    public auctionObject(){
        //no arg constructor
    }


    public auctionObject(Integer id, String user, String name, String description, Double bid, Boolean ended, Boolean fixed){
        this.id = id;
        this.user = user;
        this.name = name;
        this.description = description;
        this.bid = bid;
        this.ended = ended;
        this.fixed = fixed;


}

/*
Getters and Setters for each value in the object.
 */

    public Integer getId(){
            return id;
    }

    public String getUser(){
            return user;
    }

    public String getName(){
            return name;
    }

    public String getDescription(){
            return description;
    }

    public Double getBid(){
            return bid;
    }

    public Boolean getEnded(){
        return ended != null && ended;
    }

    public Boolean getFixed(){
        return fixed != null && fixed;
    }

    public auctionObject setId(Integer id){
            this.id = id;
            return this;
    }

    public auctionObject setName(String name){
            this.name = name;
            return this;
    }

    public auctionObject setDescription(String description){
            this.description = description;
            return this;
    }

    public auctionObject setBid(Double bid){
            this.bid = bid;
            return this;
    }

    public auctionObject setEnded(Boolean ended){
        this.ended = ended;
        return this;
    }

    public auctionObject setFixed(Boolean fixed){
        this.fixed = fixed;
        return this;
    }


    public Object[] ArrayTransform(){
        return new Object[]{
                id,
                user,
                name,
                bid,
                getEnded() ? "Unavailable" : "Available",
                getFixed() ? "Fixed Price" : "Auction"
        };
    }

}
