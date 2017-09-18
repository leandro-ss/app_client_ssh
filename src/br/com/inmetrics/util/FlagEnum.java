package br.com.inmetrics.util;

public enum FlagEnum {
    YES("S"), NO("N");

    private String flag;

    private FlagEnum (String flag) {
        this.flag = flag;
    }

    public String getFlag() {
        return flag;
    }
};