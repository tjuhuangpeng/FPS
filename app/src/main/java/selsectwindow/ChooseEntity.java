package selsectwindow;

/**
 * Created by Administrator on 2017/2/21.
 */

import java.io.Serializable;

@SuppressWarnings("serial")
public class ChooseEntity extends BaseEntity implements Serializable {
    private String sid; //经销商id
    private String name;
    private boolean isClickable = true;
    private boolean isChecked = false;
    private String colorName;

    /**
     * 导航配置的type类型
     * 1.最新,新闻,评测,导购，行情，用车，技术，文化，改装，游记，
     * 3.原创视频
     * 4.说客
     * 5.快报
     */
    private String type;
    private String param1;
    private String param2;
    private String param3;
    private int phonelength;

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    public int getPhonelength() {
        return phonelength;
    }

    public void setPhonelength(int phonelength) {
        this.phonelength = phonelength;
    }

    public ChooseEntity() {

    }

    public ChooseEntity(String Id, String Name) {
        sid = Id;
        name = Name;
    }

    public ChooseEntity(String Id, String Name, int phoneLength) {
        sid = Id;
        name = Name;
        this.phonelength = phoneLength;
    }

    public ChooseEntity(String Id, String Name, String colorName) {
        sid = Id;
        name = Name;
        this.colorName = colorName;
    }

    public ChooseEntity(String Id, String Name, boolean isClick) {
        sid = Id;
        name = Name;
        isClickable = isClick;
    }

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsClickable() {
        return isClickable;
    }

    public void setClickable(boolean isClickable) {
        this.isClickable = isClickable;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

//	public void setLocationParams(String cityName, String param1, String param2) {
//		this.name = cityName;
//		this.param1 = param1;
//		this.param2 = param2;
//	}

//	public void setBrandParams(String brandName, String param1, String param2, String param3) {
//		this.name = brandName;
//		this.param1 = param1;
//		this.param2 = param2;
//		this.param3 = param3;
//	}

    public String getParam1() {
        return param1;
    }

    public void setParam1(String param1) {
        this.param1 = param1;
    }

    public String getParam2() {
        return param2;
    }

    public void setParam2(String param2) {
        this.param2 = param2;
    }

    public String getParam3() {
        return param3;
    }

    public void setParam3(String param3) {
        this.param3 = param3;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

