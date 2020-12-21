//package com.techshard.batch.utils;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.techshard.batch.dao.entity.Voltage;
//
//
//import java.util.ArrayList;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//public class FileUploadResponse<T> {
//
//
//   // @JsonIgnore
//    private List<T> result;
//
//
//    private int successCount;
//
//    private int failedCount;
//
//
//    private Set<Voltage> voltages;
//
//    public Set<Voltage> getVoltages() {
//        return voltages;
//    }
//
//    public void setVoltages(Set<Voltage> voltages) {
//        this.voltages = voltages;
//    }
//
//    private Boolean errorFound = Boolean.FALSE;
//
//   // private String errorMessage;
//
//    private List<RowError> rowErrors = new ArrayList<>();
//
//
//
//    private Map<String, List<String>> errorMessage;
//
//
//    public Map<String, List<String>> getErrorMessage() {
//        return errorMessage;
//    }
//
//    public void setErrorMessage(Map<String, List<String>> errorMessage) {
//        this.errorMessage = errorMessage;
//    }
//
//    public List<T> getResult() {
//        return result;
//    }
//
//    public void setResult(List<T> result) {
//        this.result = result;
//    }
//
//    public Boolean getErrorFound() {
//        return errorFound;
//    }
//
//    public void setErrorFound(Boolean errorFound) {
//        this.errorFound = errorFound;
//    }
//
//    public List<RowError> getRowErrors() {
//        return rowErrors;
//    }
//
//    public void setRowErrors(RowError row) {
//        rowErrors.add(row);
//    }
//
//
//    public int getSuccessCount() {
//        return successCount;
//    }
//
//    public void setSuccessCount(int successCount) {
//        this.successCount = successCount;
//    }
//
//    public int getFailedCount() {
//        return failedCount;
//    }
//
//    public void setFailedCount(int failedCount) {
//        this.failedCount = failedCount;
//    }
//}