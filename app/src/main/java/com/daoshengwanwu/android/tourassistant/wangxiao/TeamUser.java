package com.daoshengwanwu.android.tourassistant.wangxiao;

import android.graphics.Bitmap;

/**
 * Created by HP on 2016/12/19.
 */
public class TeamUser {
    private String tuTeamID;
    private String tuID;
    private String tuName;
    private String tuTel;
    private String tuQQ;
    private String tuSex;
    private String tuEmail;
    private Bitmap tuNickname;

    public TeamUser(String tuTeamID, String tuEmail, String tuID, String tuName, Bitmap tuNickname, String tuQQ, String tuSex, String tuTel) {
        this.tuTeamID = tuTeamID;
        this.tuEmail = tuEmail;
        this.tuID = tuID;
        this.tuName = tuName;
        this.tuNickname = tuNickname;
        this.tuQQ = tuQQ;
        this.tuSex = tuSex;
        this.tuTel = tuTel;
    }

    public String getTuTeamID() {
        return tuTeamID;
    }

    public String getTuEmail() {
        return tuEmail;
    }

    public String getTuID() {
        return tuID;
    }

    public String getTuName() {
        return tuName;
    }

    public Bitmap getTuNickname() {
        return tuNickname;
    }

    public String getTuQQ() {
        return tuQQ;
    }

    public String getTuSex() {
        return tuSex;
    }

    public String getTuTel() {
        return tuTel;
    }

    public void setTuEmail(String tuEmail) {
        this.tuEmail = tuEmail;
    }

    public void setTuID(String tuID) {
        this.tuID = tuID;
    }

    public void setTuName(String tuName) {
        this.tuName = tuName;
    }

    public void setTuNickname(Bitmap tuNickname) {
        this.tuNickname = tuNickname;
    }

    public void setTuQQ(String tuQQ) {
        this.tuQQ = tuQQ;
    }

    public void setTuSex(String tuSex) {
        this.tuSex = tuSex;
    }

    public void setTuTel(String tuTel) {
        this.tuTel = tuTel;
    }

    public void setTuTeamID(String tuTeamID) {
        this.tuTeamID = tuTeamID;
    }
}
