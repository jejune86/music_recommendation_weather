package com.example.musicrecommendation.data.model;

import com.google.gson.annotations.SerializedName;

public class WeatherResponse {

    @SerializedName("numOfRows")
    private int numOfRows; // 한 페이지 결과 수

    @SerializedName("pageNo")
    private int pageNo; // 페이지 번호

    @SerializedName("totalCount")
    private int totalCount; // 데이터 총 개수

    @SerializedName("resultCode")
    private String resultCode; // 응답 메시지 코드

    @SerializedName("resultMsg")
    private String resultMsg; // 응답 메시지 내용

    @SerializedName("dataType")
    private String dataType; // 데이터 타입

    @SerializedName("baseDate")
    private String baseDate; // 발표일자

    @SerializedName("baseTime")
    private String baseTime; // 발표 시각

    @SerializedName("nx")
    private int nx; // 예보지점 X 좌표

    @SerializedName("ny")
    private int ny; // 예보지점 Y 좌표

    @SerializedName("category")
    private String category; // 자료 구분 코드

    @SerializedName("obsrValue")
    private String obsrValue; // 실황 값

    // Getter and Setter Methods
    public int getNumOfRows() {
        return numOfRows;
    }

    public void setNumOfRows(int numOfRows) {
        this.numOfRows = numOfRows;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getBaseDate() {
        return baseDate;
    }

    public void setBaseDate(String baseDate) {
        this.baseDate = baseDate;
    }

    public String getBaseTime() {
        return baseTime;
    }

    public void setBaseTime(String baseTime) {
        this.baseTime = baseTime;
    }

    public int getNx() {
        return nx;
    }

    public void setNx(int nx) {
        this.nx = nx;
    }

    public int getNy() {
        return ny;
    }

    public void setNy(int ny) {
        this.ny = ny;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getObsrValue() {
        return obsrValue;
    }

    public void setObsrValue(String obsrValue) {
        this.obsrValue = obsrValue;
    }

    // toString() 오버라이드 (디버깅용)
    @Override
    public String toString() {
        return "WeatherResponse{" +
                "numOfRows=" + numOfRows +
                ", pageNo=" + pageNo +
                ", totalCount=" + totalCount +
                ", resultCode='" + resultCode + '\'' +
                ", resultMsg='" + resultMsg + '\'' +
                ", dataType='" + dataType + '\'' +
                ", baseDate='" + baseDate + '\'' +
                ", baseTime='" + baseTime + '\'' +
                ", nx=" + nx +
                ", ny=" + ny +
                ", category='" + category + '\'' +
                ", obsrValue='" + obsrValue + '\'' +
                '}';
    }
}
