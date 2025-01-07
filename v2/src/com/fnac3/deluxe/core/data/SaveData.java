package com.fnac3.deluxe.core.data;

import java.io.Serializable;
import java.util.Map;

public class SaveData implements Serializable {

    public Map<String, Mode> modes;

    public class Mode implements Serializable{
        public int beatType;
        public String time = "0:00";
    }
}
