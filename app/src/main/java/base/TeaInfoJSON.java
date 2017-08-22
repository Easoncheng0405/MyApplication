package base;

import java.util.ArrayList;

/**
 * Created by chengjie on 17-8-22.
 */

public class TeaInfoJSON {
    private int code;
    private String status;
    private ArrayList<String> resArr;

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
}
