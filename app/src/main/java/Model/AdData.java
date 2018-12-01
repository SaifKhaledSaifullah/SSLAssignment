package Model;

public  class AdData {
    private String adName="";
    private int adLength=0;

    public AdData(String adName, int adLength) {
        if(!adName.equals("")&& !(adLength<1))
        {
            this.adName = adName;
            this.adLength = adLength;
        }

    }

    public String getAdName() {
        return adName;
    }

    public int getAdLength() {
        return adLength;
    }
}
