package com.misfit.ekyc.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NRCData {

    @SerializedName("side")
    @Expose
    public String side;
    @SerializedName("nrc_id")
    @Expose
    public NrcId nrcId;
    @SerializedName("issue_date")
    @Expose
    public IssueDate issueDate;
    @SerializedName("name")
    @Expose
    public Name name;
    @SerializedName("fathers_name")
    @Expose
    public FathersName fathersName;
    @SerializedName("birth_date")
    @Expose
    public BirthDate birthDate;
    @SerializedName("religion")
    @Expose
    public Religion religion;
    @SerializedName("height")
    @Expose
    public Height height;
    @SerializedName("blood_group")
    @Expose
    public BloodGroup bloodGroup;

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public NrcId getNrcId() {
        return nrcId;
    }

    public void setNrcId(NrcId nrcId) {
        this.nrcId = nrcId;
    }

    public IssueDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(IssueDate issueDate) {
        this.issueDate = issueDate;
    }

    public Name getName() {
        return name;
    }

    public void setName(Name name) {
        this.name = name;
    }

    public FathersName getFathersName() {
        return fathersName;
    }

    public void setFathersName(FathersName fathersName) {
        this.fathersName = fathersName;
    }

    public BirthDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(BirthDate birthDate) {
        this.birthDate = birthDate;
    }

    public Religion getReligion() {
        return religion;
    }

    public void setReligion(Religion religion) {
        this.religion = religion;
    }

    public Height getHeight() {
        return height;
    }

    public void setHeight(Height height) {
        this.height = height;
    }

    public BloodGroup getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(BloodGroup bloodGroup) {
        this.bloodGroup = bloodGroup;
    }
}