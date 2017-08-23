package base;

import java.util.ArrayList;

/**
 * Created by chengjie on 17-8-22.
 */

public class TeaInfoJSON {
    private int code;
    private String status;
    private ArrayList<String> resArr;
    private ArrayList<String> shortDescription;
    private ArrayList<String> longDescription;
    private ArrayList<String> name;
    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<String> getResArr() {
        return resArr;
    }

    public void setResArr(ArrayList<String> resArr) {
        this.resArr = resArr;
    }

    public ArrayList<String> getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(ArrayList<String> shortDescription) {
        this.shortDescription = shortDescription;
    }

    public ArrayList<String> getLongDescription() {
        return longDescription;
    }

    public void setLongDescription(ArrayList<String> longDescription) {
        this.longDescription = longDescription;
    }

    public ArrayList<String> getName() {
        return name;
    }

    public void setName(ArrayList<String> name) {
        this.name = name;
    }
}
