package com.example.bckndApi.data;

public class CommentInfo {
    private String content;
    private long measureId;
    private long doctorId;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getMeasureId() {
        return measureId;
    }

    public void setMeasureId(long measureId) {
        this.measureId = measureId;
    }

    public long getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(long doctorId) {
        this.doctorId = doctorId;
    }
}
