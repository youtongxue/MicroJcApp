package com.service.microjc.stType;

public class ToDoInfo {
    private int id;//主键
    private String title;
    private String info;
    private String local;
    private String ymdStart;
    private String start;
    private String ymdEnd;
    private String end;
    private int hm;
    private int tip;
    private String repeat;
    private String selectTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getYmdStart() {
        return ymdStart;
    }

    public void setYmdStart(String ymdStart) {
        this.ymdStart = ymdStart;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getYmdEnd() {
        return ymdEnd;
    }

    public void setYmdEnd(String ymdEnd) {
        this.ymdEnd = ymdEnd;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public int getHm() {
        return hm;
    }

    public void setHm(int hm) {
        this.hm = hm;
    }

    public int getTip() {
        return tip;
    }

    public void setTip(int tip) {
        this.tip = tip;
    }

    public String getRepeat() {
        return repeat;
    }

    public void setRepeat(String repeat) {
        this.repeat = repeat;
    }

    public String getSelectTime() {
        return selectTime;
    }

    public void setSelectTime(String selectTime) {
        this.selectTime = selectTime;
    }
}
