public final class iVariables {

    private iVariables() {
        throw new UnsupportedOperationException();
    }

    //Standard timeout for anything brought out of the space ~3 seconds
    public static final long STANDARD_TIMEOUT = 3000;

    //Name of the main window
    public static final String WINDOW_NAME = "Auction House";

    //Timeout value for Auctions, defaulting to 1 hour (60*60*1000)
    public static final long AUCTION_TIMEOUT = 3600000;

}
