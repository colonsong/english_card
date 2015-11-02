package recyclerview.android.com.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.facebook.stetho.Stetho;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

import recyclerview.android.com.myapplication.sql.Card;
import recyclerview.android.com.myapplication.sql.CardDAO;

public class MainActivity extends AppCompatActivity {

    private LayoutInflater mInflater;
    private LinearLayout mGallery;
    private View clickOptionsView;
    private int countText = 0;
    private CardDAO cardDAO;
    // 儲存所有記事本的List物件
    private List<Card> cards;
    private final String TAG = "MainActivity";
    private String[] randomBgColor = {"#E91E63","#F44336","#9C27B0","#673AB7","#3F51B5","#2196F3","#03A9F4"
            ,"#00BCD4","#009688","#4CAF50","#8BC34A","#CDDC39","#FFEB3B","#FFC107","#FF9800","#607D8B"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInflater = LayoutInflater.from(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        // 建立資料庫物件
        cardDAO = new CardDAO(getApplicationContext());

        // 如果資料庫是空的，就建立一些範例資料
        // 這是為了方便測試用的，完成應用程式以後可以拿掉
        cardDAO.deleteAll();
        if (cardDAO.getCount() == 0) {
            cardDAO.sample();
        }



        initView();
        Stetho.initializeWithDefaults(this);
    }

    private void initView()
    {
        mGallery = (LinearLayout) findViewById(R.id.main_linear_layout);
        // 取得所有記事資料
        int depth = 0;
       while(true)
       {
           cards = cardDAO.getCard(0, depth);
           if(cards.size() == 0)
           {
               break;
           }
           insertCard(getCardView(), depth);
           addCardItem(depth, cards);
           depth++;
       }









        View submitView = mInflater.inflate(R.layout.v_submit,
                mGallery, false);

        mGallery.addView(submitView);


    }

    public void addCardItem(int depth,List<Card> cards)
    {




        mGallery.getChildCount();
        LinearLayout view =   (LinearLayout) mGallery.getChildAt(depth);
        LinearLayout editLayout =  (LinearLayout) view.findViewById(R.id.editLayout);
        editLayout.removeAllViews();
        Iterator<Card> iterator = cards.iterator();
        while (iterator.hasNext()) {
            Card card = iterator.next();

            EditText edText = (EditText) mInflater.inflate(R.layout.card_item,mGallery, false);
            edText.setText(card.getWord());
            editLayout.addView(edText);
        }










    }


    public void cardOptions(View v)
    {
        clickOptionsView = v;
        Intent intent = new Intent(this, CardOptionsActivity.class);
        // 呼叫「startActivity」，參數為一個建立好的Intent物件
        // 這行敘述執行以後，如果沒有任何錯誤，就會啟動指定的元件
        startActivityForResult(intent, 0);

    }
    public void addCard()
    {

        LinearLayout addNoteLayout = (LinearLayout) ((LinearLayout)clickOptionsView.getParent()).getParent();

        int totalIndex = mGallery.getChildCount();
        int addIndexLocation = mGallery.indexOfChild(addNoteLayout);

        countText++;
        Log.v("＃＃＃＃", totalIndex + "" + addIndexLocation);
        insertCard(getCardView(), addIndexLocation + 1);




    }

    public void removeCard()
    {
        LinearLayout addNoteLayout = (LinearLayout) ((LinearLayout)clickOptionsView.getParent()).getParent();
        addNoteLayout.removeAllViews();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.v("TAG","@@");
        // 如果被啟動的Activity元件傳回確定的結果

            if (resultCode == Activity.RESULT_OK) {
                // 讀取標題
                String action = data.getStringExtra("action");

                if(action.equals("add"))
                {
                    addCard();
                }
                else
                {
                    removeCard();
                }
            }


    }

    public void insertCard(View v,int depth)
    {

        mGallery.addView(v,depth);
    }

    public View getCardView()
    {

        View view = mInflater.inflate(R.layout.v_card,
                mGallery, false);

        String colorStr = randomBgColor[new Random().nextInt(randomBgColor.length)];

        EditText edText = (EditText) view.findViewById(R.id.editText1);
        edText.setBackgroundColor(Color.parseColor(colorStr));
        edText.setText(countText+"");



       // Button insertButton = (Button) view.findViewById(R.id.add_v_card_btn);


        return view;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
