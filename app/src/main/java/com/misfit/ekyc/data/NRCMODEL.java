package com.misfit.ekyc.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NRCMODEL {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("data")
    @Expose
    private NRCData data;
    @SerializedName("ref_id")
    @Expose
    private Integer refId;

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public NRCData getData() {
        return data;
    }

    public void setData(NRCData data) {
        this.data = data;
    }

    public Integer getRefId() {
        return refId;
    }

    public void setRefId(Integer refId) {
        this.refId = refId;
    }

    @Override
    public String toString() {
        return "NRCMODEL{" +
                "success=" + success +
                ", data=" + data +
                ", refId=" + refId +
                '}';
    }
}