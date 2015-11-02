package recyclerview.android.com.myapplication.sql;

public class Card implements java.io.Serializable {

    // 編號、日期時間、顏色、標題、內容、檔案名稱、經緯度、修改、已選擇
    private long id;
    private long cardID;
    private long datetime;

    private String word;
    private int depth;
    private long lastModify;

    public Card() {
        word = "";
        depth = -1;
    }

    public Card(long id, long datetime, String word,
                long lastModify,int depth,long cardID) {
        this.id = id;
        this.datetime = datetime;
        this.depth = depth;
        this.word = word;
        this.lastModify = lastModify;
        this.cardID =  cardID;
    }

    public long getCardID()
    {
        return cardID;
    }

    public long setCardID()
    {
        return cardID;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getDatetime() {
        return datetime;
    }

    public int getDepth()
    {
        return depth;
    }

    public void setDepth(int depth)
    {
        this.depth = depth;
    }

    public void setDatetime(long datetime) {
        this.datetime = datetime;
    }


    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }


    public long getLastModify() {
        return lastModify;
    }

    public void setLastModify(long lastModify) {
        this.lastModify = lastModify;
    }

}