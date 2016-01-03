package waterdrop.tw.catchcard.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Checkable;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import recyclerview.android.com.myapplication.R;
import waterdrop.tw.catchcard.MainActivity;
import waterdrop.tw.catchcard.sql.Card;
import waterdrop.tw.catchcard.sql.CardDAO;

public class CardListViewActivity extends AppCompatActivity {
    private CardAdapter mCardAdapter;
    private CardDAO cardDAO;
    private LayoutInflater mInflater;
    private ListView cardListView;
    private boolean selectedMenu = false;
    ProgressDialog dialog ;
    private  FloatingActionButton fab;
    public static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    public static final String TAG = "CardListViewActivity";

    private List<Integer> cardIdList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_list_view);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        mInflater = LayoutInflater.from(this);

        cardListView = (ListView)findViewById(R.id.cardListView);
        setSelectedMode();

        cardDAO = new CardDAO(getApplicationContext());

        if (cardDAO.getCount() == 0) {
            cardDAO.sample();
        }
        cardIdList = cardDAO.getCardIdList();



        //Stetho.initializeWithDefaults(this);

        mCardAdapter = new CardAdapter(this, R.layout.card_lsit_view_item, cardDAO,cardIdList);



        cardListView.setChoiceMode(AbsListView.CHOICE_MODE_NONE);

        cardListView.setAdapter(mCardAdapter);
        cardListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.setClass(CardListViewActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("position", cardIdList.get(position));
                bundle.putString("action", "editCard");
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });

    }

    private void addItemMode()
    {
        selectedMenu = false;

        fab.setImageResource(R.drawable.ic_new);
        cardListView.setChoiceMode(AbsListView.CHOICE_MODE_NONE);
        cardListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {

                Intent intent = new Intent();
                intent.setClass(CardListViewActivity.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("position", cardIdList.get(pos));
                bundle.putString("action", "editCard");
                intent.putExtras(bundle);
                startActivity(intent);


            }
        });
    }

    private void setSelectedMode()
    {
        fab = (FloatingActionButton) findViewById(R.id.addCardBtn);
        fab.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                if (selectedMenu) {
                    addItemMode();


                } else {
                    selectedMenu = true;
                    Snackbar.make(v, "進入多選刪除模式", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    fab.setImageResource(R.drawable.ic_card_rotate);

                    cardListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
                    cardListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {


                            mCardAdapter.notifyDataSetChanged();
                        }
                    });


                }

                invalidateOptionsMenu();
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();  // Always call the superclass method first

        mCardAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        Log.v(TAG, "onPrepareOptionsMenu");
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        if(selectedMenu)
        {
            getMenuInflater().inflate(R.menu.selected_menu, menu);
        }
        else
        {
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }

        Log.v(TAG, "onCreateOptionsMenu");
        return true;
    }



    public String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
/*
        RequestBody body = new FormEncodingBuilder()
                .add("platform", "android")
                .add("name", "bug")
                .add("subject", "XXXXXXXXXXXXXXX")
                .build();
                */

        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
       // Log.v(TAG,response.body().string());
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), "上傳成功", Toast.LENGTH_SHORT).show();
            }
        });


        return response.body().string();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        AbsListView list = (AbsListView) cardListView;
        switch(id)
        {
            case R.id.upload_cards_btn:
                Log.v(TAG, "upload_cards_btn");



                    new Thread(){
                        public void run()
                        {
                            JSONArray dbJson = getResults();
                            try {
                                Log.d(TAG,dbJson.toString());
                                post("http://waterdrop.tw/mobile/set_card_json", dbJson.toString());

                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }}.start();




                break;
            case R.id.download_cards_btn:


                dialog = ProgressDialog.show(this,
                        "讀取中", "請等待3秒...",true);
                new  DownloadCardTask().execute();



                break;
            case R.id.cancel_selected_btn:
                SparseBooleanArray array = list.getCheckedItemPositions();
                Log.v(TAG, "cancel_selected_btn: " + array.toString());
                for (int i = 0; i < array.size(); i++) {
                    int position = array.keyAt(i);
                    Log.v( TAG," GET " + i +  ":" +   array.get(i) );
                    Log.v( TAG," keyat " + i +  ":" +   array.keyAt(i) );
                    Log.v( TAG," vlaueat " + i +  ":" +   array.valueAt(i) );

                    Log.v(TAG, " position " + position);
                    LinearLayout getView = (LinearLayout) cardListView.getChildAt(position);

                    getView.setBackgroundResource(R.color.unread_bgcolor);

                }
                if(list != null)
                {
                    list.clearChoices();
                }

                mCardAdapter.notifyDataSetChanged();
                break;
            case R.id.del_selected_btn:


                SparseBooleanArray array2 = list.getCheckedItemPositions();

                Log.v(TAG,cardIdList.toString());


                Log.v(TAG, "del_selected_btn" + array2.toString());

                Log.v(TAG,cardIdList.toString());
                LinkedList cardIdLinkList = new LinkedList();
                for (int i = 0; i < array2.size(); i++) {

                    int position = array2.keyAt(i);
                    if (array2.get(position)) {
                        //避免recycle view 變成不同顏色

                       //從cardIdList 取的該position的cardId是多少
                        int carId = cardIdList.get(position);
                        cardIdLinkList.add(carId);
                        //刪除DB BY CARDID
                        cardDAO.removeAllByCardId(carId);




                    }

                }
                for(Iterator iter = cardIdLinkList.iterator(); iter.hasNext();)
                {
                    cardIdList.remove(cardIdList.indexOf(iter.next()));
                }

                Log.v(TAG,cardIdList.toString());
                if(list != null)
                {
                    list.clearChoices();
                }


                /*
                List<Integer> tmpCardIDList = new ArrayList<>(cardIdList);

                cardIdList.removeAll(tmpCardIDList);

                Iterator caridListIterator = tmpCardIDList.iterator();
                while(caridListIterator.hasNext()){
                    final int cardID = (int) caridListIterator.next();
                    cardIdList.add(cardID);
                }
                */




                addItemMode();
                list.requestLayout();
                mCardAdapter.notifyDataSetChanged();
                invalidateOptionsMenu();


                break;
            default:
                return super.onOptionsItemSelected(item);

        }
        return true;



    }
    public class CardAdapter extends BaseAdapter implements Checkable {

        // 畫面資源編號
        private int resource;
        // 包裝的記事資料
        private CardDAO cardDAO;
        private List<Card> cards;
        private List<Integer> cardIdList;
        private Context mContext;
        boolean mChecked = false;

        public CardAdapter(Context context, int resource, CardDAO cardDAO , List<Integer> cardIdList) {

            this.mContext = context;
            this.cardDAO = cardDAO;
            this.resource = resource;
            this.cardIdList = cardIdList;


        }

        @Override
        public int getCount() {
            Log.v(TAG,"size" + cardIdList.size());
            return cardIdList.size();
        }

        @Override
        public List<Card> getItem(int position) {

            int cardId = cardIdList.get(position);
            return cardDAO.getCard(cardId, 0);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LinearLayout itemLayout;
            // 讀取目前位置的記事物件
             cards =  getItem(position);

            if (convertView == null) {
                // 建立項目畫面元件

                 itemLayout =  (LinearLayout) mInflater.inflate(R.layout.card_lsit_view_item, parent, false);



            }
            else {
                itemLayout = (LinearLayout) convertView;
            }

            TextView itemText = (TextView) itemLayout.findViewById(R.id.cardTextView);
            updateBackground(position , itemText);
            Iterator<Card> iterator = cards.iterator();
            String wordStr = "";
            while (iterator.hasNext()) {
                Card card = iterator.next();

                wordStr = wordStr + "、"  + card.getWord();




            }



            itemText.setText(wordStr);



            return itemLayout;
        }
        public void updateBackground(int position, View view) {
            if (cardListView.isItemChecked(position)) {
                view.setBackgroundResource(R.color.colorListSelected);
                Log.v(TAG, position + "  1111111111111");
            } else {
                view.setBackgroundResource(R.color.unread_bgcolor );
                Log.v(TAG, position + "   22222222222");
            }
        }


        @Override
        public void setChecked(boolean c) {
            mChecked = c;
            if (mChecked) {

            } else {

            }
        }

        @Override
        public boolean isChecked() {
            return mChecked;
        }

        @Override
        public void toggle() {
            setChecked(!mChecked);
        }
    }





    public void addCardBtn(View v)
    {
        Intent intent = new Intent();
        intent.setClass(CardListViewActivity.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("position", cardDAO.getNewCardID());
        bundle.putString("action", "newCard");
        intent.putExtras(bundle);
        startActivity(intent);

    }


    private JSONArray getResults()
    {

        String myPath = "/data/data/waterdrop.tw.catchcard/databases/card.db";// Set path to your database

        String myTable = "card";//Set name of your table

        SQLiteDatabase myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);

        String searchQuery = "SELECT  * FROM " + myTable;
        Cursor cursor = myDataBase.rawQuery(searchQuery, null );

        JSONArray resultSet     = new JSONArray();

        cursor.moveToFirst();
        while (cursor.isAfterLast() == false) {

            int totalColumn = cursor.getColumnCount();
            JSONObject rowObject = new JSONObject();

            for( int i=0 ;  i< totalColumn ; i++ )
            {
                if( cursor.getColumnName(i) != null )
                {
                    try
                    {
                        if( cursor.getString(i) != null )
                        {
                            Log.d("TAG_NAME", cursor.getString(i) );
                            rowObject.put(cursor.getColumnName(i) ,  cursor.getString(i) );
                        }
                        else
                        {
                            rowObject.put( cursor.getColumnName(i) ,  "" );
                        }
                    }
                    catch( Exception e )
                    {
                        Log.d("TAG_NAME", e.getMessage()  );
                    }
                }
            }
            resultSet.put(rowObject);
            cursor.moveToNext();
        }
        cursor.close();
        Log.d("TAG_NAME", resultSet.toString() );
        return resultSet;
    }


    class  DownloadCardTask  extends AsyncTask<Void, Integer, Boolean> {

        @Override
        protected  void  onPreExecute() {
            dialog.show();
        }

        @Override
        protected  Boolean doInBackground(Void... params) {
            Iterator delCardId = cardDAO.getCardIdList().iterator();
            while(delCardId.hasNext()){
                final int card_id = (int) delCardId.next();
                cardIdList.remove(cardIdList.indexOf(card_id));
            }



            cardDAO.deleteAll();
            try {

                OkHttpClient client = new OkHttpClient();
                Request request = new Request.Builder()
                        .url("http://waterdrop.tw/mobile/get_card_json/")
                        .build();
                Response responses = null;

                try {
                    responses = client.newCall(request).execute();
                    if(! responses.isSuccessful())
                    {
                        return false;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String jsonData = responses.body().string();
                Log.v(TAG,jsonData.toString());
                JSONObject Jobject = new JSONObject(jsonData);
                Log.v(TAG,Jobject.toString());
                JSONArray Jarray = Jobject.getJSONArray("data");

                for (int i = 0; i < Jarray.length(); i++) {
                    publishProgress(i);
                    JSONObject object  = Jarray.getJSONObject(i);
                    Log.v(TAG,object.toString());
                    String card_id = object.get("card_id").toString();
                    String word = object.get("word").toString();
                    String depth = object.get("depth").toString();

                    Card card0Item0  = new Card(0, new Date().getTime(),word, 0, Integer.parseInt(depth),Long.parseLong(card_id));

                   //

                    cardDAO.insert(card0Item0);



                    //Log.v(TAG,word);
                }
                Iterator delCardId2 = cardDAO.getCardIdList().iterator();
                while(delCardId2.hasNext()){
                    final int card_id = (int) delCardId2.next();
                    cardIdList.add(card_id);
                }

                Log.v(TAG, cardIdList.toString());


            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        protected  void  onProgressUpdate(Integer... values) {
            dialog.setMessage( "當前下載進度："  + values[ 0 ] +  "%" );
        }

        @Override
        protected  void  onPostExecute(Boolean result) {
            dialog.dismiss();
            mCardAdapter.notifyDataSetChanged();
            if  (result) {
                Toast.makeText(getApplicationContext(),  "下載成功" , Toast.LENGTH_SHORT).show();
            }  else  {
                Toast.makeText(getApplicationContext(),  "下載失敗" , Toast.LENGTH_SHORT).show();
            }
        }
    }

}



