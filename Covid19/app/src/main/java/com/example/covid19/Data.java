package com.example.covid19;

public class Data {
    String loc,Con,Rec,Det;

    public Data(String loc, String con, String rec, String det) {
        this.loc = loc;
        Con = con;
        Rec = rec;
        Det = det;
    }

    public String getLoc() {
        return loc;
    }

    public String getCon() {
        return Con;
    }

    public String getRec() {
        return Rec;
    }

    public String getDet() {
        return Det;
    }
}
