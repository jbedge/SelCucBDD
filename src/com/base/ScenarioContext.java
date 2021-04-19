package com.base;

import java.util.HashMap;
import java.util.Map;

public class ScenarioContext {

    private Map<String,Object> scenarioContext;
    public ScenarioContext() {scenarioContext=new HashMap<>();
    }
    public void setContext(Context key, Object val){scenarioContext.put(key.toString(),val);}
    public Object getContext(Context key){return scenarioContext.get(key.toString());}
    public Boolean isContains(Context key){return scenarioContext.containsKey(key.toString());}
}
