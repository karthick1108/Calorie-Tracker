package com.example.fit5046_assignment2;

import java.util.Date;

public class Credential {

    private String usrname;
    private String usrpwd;
    private Date signupdate;
    private Appuser usrid;


    public Credential(Appuser usrid, String usrname, String usrpwd, Date signupdate) {
        this.usrid= usrid;
        this.usrname = usrname;
        this.usrpwd = usrpwd;
        this.signupdate = signupdate;
    }

    public Credential() {
    }

    public String getUsrname() {
        return usrname;
    }

    public void setUsrname(String usrname) {
        this.usrname = usrname;
    }

    public String getUsrpwd() {
        return usrpwd;
    }

    public void setUsrpwd(String usrpwd) {
        this.usrpwd = usrpwd;
    }

    public Date getSignupdate() {
        return signupdate;
    }

    public void setSignupdate(Date signupdate) {
        this.signupdate = signupdate;
    }

    public Appuser getUsrid() {
        return usrid;
    }

    public void setUsrid(Appuser usrid) {
        this.usrid = usrid;
    }

}
