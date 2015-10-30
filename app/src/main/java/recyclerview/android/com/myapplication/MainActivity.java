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

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private LayoutInflater mInflater;
    private LinearLayout mGallery;
    private View clickOptionsView;
    private int countText = 0;

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

        initView();
    }

    private void initView()
    {
        mGallery = (LinearLayout) findViewById(R.id.main_linear_layout);
        insertCard(getCardView(), 0);

        View submitView = mInflater.inflate(R.layout.v_submit,
                mGallery, false);

        mGallery.addView(submitView);

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

    public void insertCard(View v,int cardIndex)
    {

        mGallery.addView(v,cardIndex);
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
