package com.example.fit5046_assignment2;

import java.util.Date;

public class Appuser {

    private Integer usrid;
    private String usremail;
    private Date usrdob;
    private String usrfname;
    private String usrsname;
    private int usrheight;
    private int usrweight;
    private Character usrgender;
    private String usraddress;
    private int usrpostcode;
    private int usrlevel;
    private int usrsteps;


    public Appuser(Integer usrid, String usremail, Date usrdob, String usrfname, String usrsname, int usrheight, int usrweight, Character usrgender, String usraddress, int usrpostcode, int usrlevel, int usrsteps) {
        this.usrid = usrid;
        this.usremail = usremail;
        this.usrdob = usrdob;
        this.usrfname = usrfname;
        this.usrsname = usrsname;
        this.usrheight = usrheight;
        this.usrweight = usrweight;
        this.usrgender = usrgender;
        this.usraddress = usraddress;
        this.usrpostcode = usrpostcode;
        this.usrlevel = usrlevel;
        this.usrsteps = usrsteps;
    }

    public Appuser()
    {

    }
    public Integer getUsrid() {
        return usrid;
    }

    public void setUsrid(Integer usrid) { this.usrid = usrid; }

    public String getUsremail() {
        return usremail;
    }

    public void setUsremail(String usremail) {
        this.usremail = usremail;
    }

    public Date getUsrdob() {
        return usrdob;
    }

    public void setUsrdob(Date usrdob) {
        this.usrdob = usrdob;
    }

    public String getUsrfname() {
        return usrfname;
    }

    public void setUsrfname(String usrfname) {
        this.usrfname = usrfname;
    }

    public String getUsrsname() {
        return usrsname;
    }

    public void setUsrsname(String usrsname) {
        this.usrsname = usrsname;
    }

    public int getUsrheight() {
        return usrheight;
    }

    public void setUsrheight(int usrheight) {
        this.usrheight = usrheight;
    }

    public int getUsrweight() {
        return usrweight;
    }

    public void setUsrweight(int usrweight) {
        this.usrweight = usrweight;
    }

    public Character getUsrgender() {
        return usrgender;
    }

    public void setUsrgender(Character usrgender) {
        this.usrgender = usrgender;
    }

    public String getUsraddress() {
        return usraddress;
    }

    public void setUsraddress(String usraddress) {
        this.usraddress = usraddress;
    }

    public int getUsrpostcode() {
        return usrpostcode;
    }

    public void setUsrpostcode(int usrpostcode) {
        this.usrpostcode = usrpostcode;
    }

    public int getUsrlevel() {
        return usrlevel;
    }

    public void setUsrlevel(int usrlevel) {
        this.usrlevel = usrlevel;
    }

    public int getUsrsteps() {
        return usrsteps;
    }

    public void setUsrsteps(int usrsteps) {
        this.usrsteps = usrsteps;
    }

}
