public class FrequentFlyer{
    private String user;
    private int miles;
    public FrequentFlyer(){}
    public FrequentFlyer(String userIn, int milesIn){
        this.user=userIn;
        this.miles=milesIn;
    }
    public String getUser(){
        return this.user;
    }
    public int getMiles(){
        return this.miles;
    }
}