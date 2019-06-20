package com.kliff.scorecard.model;

public class ScoreSummaryModel {

    private String ball_text;
    private String cms_text;
    private String full_commentary;

    public void setBall_text(String ball_text) {
        this.ball_text = ball_text;
    }

    public void setCms_text(String cms_text) {
        this.cms_text = cms_text;
    }

    public void setFull_commentary(String full_commentary) {
        this.full_commentary = full_commentary;
    }

    public String getBall_text() {
        return ball_text;
    }

    public String getCms_text() {
        return cms_text;
    }

    public String getFull_commentary() {
        return full_commentary;
    }
}
