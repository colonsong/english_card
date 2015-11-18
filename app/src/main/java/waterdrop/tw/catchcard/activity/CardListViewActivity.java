package waterdrop.tw.catchcard.activity;

import android.content.Context;
import android.content.Intent;
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

import java.util.Iterator;
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

    private void setSelectedMode()
    {
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.addCardBtn);
        fab.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                if(selectedMenu)
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
                else {
                    selectedMenu = true;
                    Snackbar.make(v, "進入多選刪除模式", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                    fab.setImageResource(R.drawable.ic_card_rotate);

                    cardListView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
                    cardListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {


                            AbsListView list = (AbsListView) adapterView;

                            SparseBooleanArray array = list.getCheckedItemPositions();


                            Log.v(TAG, array.toString());


                            for (int i = 0; i < array.size(); i++) {
                                int key = array.keyAt(i);
                                Log.v(TAG, "key: " + key);
                                View getView = (View) cardListView.getChildAt(key);
                                Log.v(TAG, "getkey :" + array.get(key) + "");
                                if (array.get(key)) {
                                    //view.setBackgroundColor(Color.parseColor("#BBDEFB"));
                                    getView.setBackgroundResource(R.color.colorListSelected);
                                    //del
                                    Log.v(TAG, "del" + key + " TRUE @ " + array.get(key));
                                } else {
                                    getView.setBackgroundColor(0);
                                    //view.setBackgroundColor(Color.GREEN);
                                    Log.v(TAG, "del" + key + " FALSE @ " + array.get(key));
                                }

                            }


                        }
                    });
                    mCardAdapter.notifyDataSetChanged();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        AbsListView list = (AbsListView) cardListView;
        switch(id)
        {
            case R.id.cancel_selected_btn:



                SparseBooleanArray array = list.getCheckedItemPositions();


                Log.v(TAG, "cancel_selected_btn: " + array.toString());

                for (int i = 0; i < array.size(); i++) {
                    int key = array.keyAt(i);

                    LinearLayout getView = (LinearLayout) cardListView.getChildAt(key);

                    if (array.get(key)) {

                        list.setItemChecked(key,false);

                        getView.setBackgroundResource(0);

                    }

                }
                mCardAdapter.notifyDataSetChanged();
                break;

            case R.id.del_selected_btn:


                SparseBooleanArray array2 = list.getCheckedItemPositions();


                Log.v(TAG, "del_selected_btn" + array2.toString());

                for (int i = 0; i < array2.size(); i++) {
                    int key = array2.keyAt(i);
                    if (array2.get(key)) {
                        cardDAO.removeAllByCardId(cardIdList.get(key));
                    }







                }
                mCardAdapter.notifyDataSetChanged();


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
            return cardDAO.getTotalCountByCardId(0);
        }

        @Override
        public List<Card> getItem(int position) {


            return cardDAO.getCard(cardIdList.get(position),0);
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

            Iterator<Card> iterator = cards.iterator();
            String wordStr = "";
            while (iterator.hasNext()) {
                Card card = iterator.next();

                wordStr = wordStr + "、"  + card.getWord();





            }



            itemText.setText(wordStr);



            return itemLayout;
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

}



