package br.com.inmetrics.util;

import java.util.Random;

public class Constant {

    public static final Integer PORT_TUNNEL = (new Random().nextInt(40000) + 8922);
    
    public static final String LOCALHOST= "localhost";
}