package com.example.hserun;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Counter {
    public static List<Integer> solved = new ArrayList<Integer>();
    public static Map<Integer, String> hints = new HashMap<Integer, String>();
    private static Counter instance;
    public static int score = 0;
    private Counter(){}
    public static Counter getInstance(){ // #3
        if(instance == null){		//если объект еще не создан
            instance = new Counter();	//создать новый объект
        }
        return instance;		// вернуть ранее созданный объект
    }
}
