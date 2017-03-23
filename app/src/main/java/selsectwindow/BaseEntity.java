package selsectwindow;

/**
 * Created by Administrator on 2017/2/21.
 */
import java.io.Serializable;


/**
 * Entity基类
 *
 */
@SuppressWarnings("serial")
public class BaseEntity  implements Serializable {

    private int id;				//ID

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int type;			//type 类型


    public void setType(int type) {
        this.type = type;
    }

    /**块的位置*/
    private int blockposition;

    public int getBlockposition() {
        return blockposition;
    }

    public void setBlockposition(int blockposition) {
        this.blockposition = blockposition;
    }

    /**
     * 用于布局使用类型判断的type
     */
    private int viewType;

    /**
     * 设置type类型   仅限于搜索 常量见
     * @param type
     */
    public void setViewType(int type){
        this.viewType = type;
    }

    public int getViewType(){
        return this.viewType;
    }
    static final long serialVersionUID =-6795987482202293249L;
}
