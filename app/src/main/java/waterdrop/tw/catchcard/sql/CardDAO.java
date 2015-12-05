package waterdrop.tw.catchcard.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ColonPc on 2015/11/1.
 */
public class CardDAO {
    // 表格名稱    
    public static final String TABLE_NAME = "card";

    // 編號表格欄位名稱，固定不變
    public static final String KEY_ID = "_id";
    public static final String TAG = "CardDAO";
    // 其它表格欄位名稱
    public static final String DATETIME_COLUMN = "datetime";
    public static final String WORD_COLUMN = "word";
    public static final String DEPTH_COLUMN = "depth";
    public static final String LASTMODIFY_COLUMN = "lastmodify";
    public static final String CARDID_COLUMN = "card_id";

    // 使用上面宣告的變數建立表格的SQL指令
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    CARDID_COLUMN + " INTEGER NOT NULL, " +
                    DEPTH_COLUMN + " TEXT NOT NULL, " +
                    WORD_COLUMN + " INTEGER NOT NULL, " +
                    DATETIME_COLUMN + " INTEGER NOT NULL, " +
                    LASTMODIFY_COLUMN + " INTEGER)";

    // 資料庫物件    
    private SQLiteDatabase db;

    // 建構子，一般的應用都不需要修改
    public CardDAO(Context context) {
        db = waterdrop.tw.catchcard.sql.CardDB.getDatabase(context);
    }

    // 關閉資料庫，一般的應用都不需要修改
    public void close() {
        db.close();
    }

    // 新增參數指定的物件
    public Card insert(Card item) {
        // 建立準備新增資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的新增資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料
        cv.put(CARDID_COLUMN, item.getCardID());
        cv.put(DEPTH_COLUMN, item.getDepth());
        cv.put(WORD_COLUMN, item.getWord());
        cv.put(DATETIME_COLUMN, item.getDatetime());
        cv.put(LASTMODIFY_COLUMN, item.getLastModify());

        // 新增一筆資料並取得編號
        // 第一個參數是表格名稱
        // 第二個參數是沒有指定欄位值的預設值
        // 第三個參數是包裝新增資料的ContentValues物件
        long id = db.insert(TABLE_NAME, null, cv);

        // 設定編號
        item.setId(id);
        // 回傳結果
        return item;
    }
    public boolean removeAllByCardId(int cardId)
    {

        String where = CARDID_COLUMN + "=" + cardId;
        return db.delete(TABLE_NAME, where , null) > 0;
    }
    // 修改參數指定的物件
    public boolean update(Card item) {
        // 建立準備修改資料的ContentValues物件
        ContentValues cv = new ContentValues();

        // 加入ContentValues物件包裝的修改資料
        // 第一個參數是欄位名稱， 第二個參數是欄位的資料        
        cv.put(DATETIME_COLUMN, item.getDatetime());

        cv.put(WORD_COLUMN, item.getWord());

        cv.put(LASTMODIFY_COLUMN, item.getLastModify());

        // 設定修改資料的條件為編號
        // 格式為「欄位名稱＝資料」
        String where = KEY_ID + "=" + item.getId();

        // 執行修改資料並回傳修改的資料數量是否成功
        return db.update(TABLE_NAME, cv, where, null) > 0;
    }

    // 刪除參數指定編號的資料
    public boolean delete(long id){
        // 設定條件為編號，格式為「欄位名稱=資料」
        String where = KEY_ID + "=" + id;
        // 刪除指定編號資料並回傳刪除是否成功
        return db.delete(TABLE_NAME, where, null) > 0;
    }

    // 刪除參數指定編號的資料
    public boolean deleteAll(){
        // 刪除指定編號資料並回傳刪除是否成功
        return db.delete(TABLE_NAME, null , null) > 0;
    }

    // 讀取所有記事資料
    public List<Card> getAll() {
        List<Card> result = new ArrayList<>();
        Cursor cursor = db.query(
                TABLE_NAME, null, null, null, null, null, null, null);

        while (cursor.moveToNext()) {
            Card c = getRecord(cursor);
            result.add(c);
        }

        cursor.close();
        return result;
    }

    public List<Integer> getCardIdList()
    {
        int position = 0;
        List<Integer> result = new ArrayList<>(30);
        // 使用編號為查詢條件
        String groupBy = CARDID_COLUMN;
        // 執行查詢
        Cursor cursor = db.query(
                TABLE_NAME, null, null, null, groupBy, null, null, null);

        while (cursor.moveToNext()) {
            result.add(position, cursor.getInt(1));
        }

        cursor.close();
        return result;
    }

    // 取得指定編號的資料物件
    public List<Card> getCard(int cardId,int depth) {
        // 準備回傳結果用的物件
        List<Card> result = new ArrayList<>();
        // 使用編號為查詢條件
        String where = CARDID_COLUMN + "=" + cardId + " AND " + DEPTH_COLUMN + "=" + depth;
        // 執行查詢
        Cursor cursor = db.query(
                TABLE_NAME, null, where, null, null, null, null, null);

        while (cursor.moveToNext()) {
            Card c = getRecord(cursor);
            result.add(c);
        }

        cursor.close();
        return result;
    }



    public int getNewCardID()
    {
        // 準備回傳結果用的物件
        Card item = null;
        // 使用編號為查詢條件
        String orderBy = CARDID_COLUMN + " DESC ";
        // 執行查詢
        Cursor result = db.query(
                TABLE_NAME, null, null, null, null, null, orderBy, null);

        // 如果有查詢結果

        if (result.moveToFirst()) {
            // 讀取包裝一筆資料的物件
            item = getRecord(result);            Log.d(TAG,"@@ cardID:" + item.getId()  );
        }

        // 關閉Cursor物件
        result.close();

        // 回傳結果
        if(item != null)
        {
            return (int)item.getCardID() +1 ;
        }
        else
        {
            return 0;
        }

    }

    // 取得指定編號的資料物件
    public Card get(long id) {
        // 準備回傳結果用的物件
        Card item = null;
        // 使用編號為查詢條件
        String where = KEY_ID + "=" + id;
        // 執行查詢
        Cursor result = db.query(
                TABLE_NAME, null, where, null, null, null, null, null);

        // 如果有查詢結果
        if (result.moveToFirst()) {
            // 讀取包裝一筆資料的物件
            item = getRecord(result);
        }

        // 關閉Cursor物件
        result.close();
        // 回傳結果
        return item;
    }

    // 把Cursor目前的資料包裝為物件
    public Card getRecord(Cursor cursor) {
        // 準備回傳結果用的物件
        Card result = new Card();

        result.setId(cursor.getLong(0));
        result.setDatetime(cursor.getLong(4));
        result.setCardID(cursor.getInt(1));
        result.setWord(cursor.getString(3));
        result.setDepth(cursor.getInt(2));
        result.setLastModify(cursor.getLong(5));

        // 回傳結果
        return result;
    }
    public int getTotalCountByCardId(int depth)
    {
        int result = 0;
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME + " WHERE " + DEPTH_COLUMN + " = " + depth + " group by " + CARDID_COLUMN, null);

        if (cursor.moveToNext()) {
            result = cursor.getCount();
        }

        return result;
    }

    // 取得資料數量
    public int getCount() {
        int result = 0;
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM " + TABLE_NAME, null);

        if (cursor.moveToNext()) {
            result = cursor.getInt(0);
        }

        return result;
    }

    // 建立範例資料
    public void sample() {

        /**
         *  Card(long id, long datetime, String word,
         long lastModify,int depth,long cardID) {
         */
        Card card0Item0  = new Card(0, new Date().getTime(), "大方形", 0, 0,0);
        Card card0Item1 = new Card(0, new Date().getTime(), "有四腳", 0, 0,0);
        Card card0Item2 = new Card(0, new Date().getTime(), "布", 0, 0,0);

        insert(card0Item0);
        insert(card0Item1);
        insert(card0Item2);

        Card card1Item0  = new Card(0, new Date().getTime(), "Windows", 0, 1,0);
        insert(card1Item0);



    }
}
