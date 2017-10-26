package onetwopunch.seoulinsangshot.com.seoulinsangshot.View;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterViewFlipper;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import onetwopunch.seoulinsangshot.com.seoulinsangshot.Controller.BottomNavigationViewHelper;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.Model.Model_Main;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.R;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener,CustomDialog.OnCompleteListener {

    Intent primary;
    Intent album;
    Intent notify;
    Intent detail;

    //avf를 위한 부분
    AdapterViewFlipper avf;
    ImageView mainImage;
    TextView hitTv;
    TextView themeTv;
    TextView nameKorean;
    TextView nameEnglish;
    ArrayList<ImageView> indexes = new ArrayList<ImageView>();
    ArrayList<Model_Main> tempList = new ArrayList<Model_Main>();
    RelativeLayout mainRelativeLayout;

    CustomDialog customDialog = CustomDialog.getInstance();
    //온터치리스너를 위한 처리 실수
    float xAtDown;
    float xAtUp;

    private BottomNavigationView navigation;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    return true;
                case R.id.navigation_search:
                    setIntentFlag(primary);
                    startActivity(primary);
                    return true;
                case R.id.navigation_album:
                    if(BaseActivity.isLogined == 0){
                        FragmentManager fm = getSupportFragmentManager();
                        customDialog.show(fm, "Login Dialog");
                    }else {
                        if(!BaseActivity.imageList.isEmpty()) {
                            setIntentFlag(album);
                            startActivity(album);
                        }else{
                            String msg = "앨범에 사진이 없습니다.";
                            if(!BaseActivity.isKorean) msg = "No picture in your album.";
                            Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                        }
                    }
                    return true;
                case R.id.navigation_notifications:
                    setIntentFlag(notify);
                    startActivity(notify);
                    return true;
                case R.id.navigation_login:
                    FragmentManager fm = getSupportFragmentManager();
                    customDialog.show(fm, "Login Dialog");
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#050518")));

        Drawable d = getResources().getDrawable(R.drawable.actionbar);
        getSupportActionBar().setBackgroundDrawable(d);

        primary = new Intent(getApplicationContext(), PrimaryActivity.class);
        album = new Intent(getApplicationContext(), AlbumActivity.class);
        notify = new Intent(getApplicationContext(), NotifyActivity.class);
        detail = new Intent(getApplicationContext(),DetailActivity.class);

        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setSelectedItemId(R.id.navigation_home);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        //////////////////////////////////////////////////////////////

        ///////////////// AVF를 위한 부분 /////////////////////////////

        /////////////////////////////////////////////////////////////

        indexes.add((ImageView) findViewById(R.id.indexLight1));
        indexes.add((ImageView) findViewById(R.id.indexLight2));
        indexes.add((ImageView) findViewById(R.id.indexLight3));
        indexes.add((ImageView) findViewById(R.id.indexLight4));
        indexes.add((ImageView) findViewById(R.id.indexLight5));
        indexes.add((ImageView) findViewById(R.id.indexLight6));
        indexes.add((ImageView) findViewById(R.id.indexLight7));


        nameKorean = (TextView) findViewById(R.id.main_areaName);
        nameEnglish=(TextView)findViewById(R.id.main_areaName2);
        hitTv = (TextView) findViewById(R.id.main_hitText);
        themeTv = (TextView) findViewById(R.id.main_theme1);
        mainRelativeLayout=(RelativeLayout)findViewById(R.id.mainRL);

        //아직 메인을 위한 서버가 없으므로
//        tempList.add(BaseActivity.testArr.get(0));
//        tempList.add(BaseActivity.testArr.get(1));
//        tempList.add(BaseActivity.testArr.get(2));
//        tempList.add(BaseActivity.testArr.get(3));
//        tempList.add(BaseActivity.testArr.get(4));

        /*
            우선 for문을 통해서 base에서 로드가 안 됬다면 아예 대표사진도 안 뜨도록 설정함.
         */
        for (int i = 0; i < BaseActivity.mainList.size(); i++) {
            tempList.add(BaseActivity.mainList.get(i));
            if (i == 6) {
                i = BaseActivity.mainList.size();
            }
        }



        //어뎁터 연결 ,널포인터 뜰 수 있음.
        avf = (AdapterViewFlipper) findViewById(R.id.main_AVF);
        avf.setAdapter(new galleryAdapter(this, tempList)); //갤러리 어뎁터에 연결
        avf.setOnTouchListener(this);//터치리스너에 연결

        //onCreate 종료
    }
    @Override
    public void onInputedData(String name, String email) {

        BaseActivity.email=email;
        BaseActivity.name=name;

        System.out.println("name, email"+BaseActivity.name+" , "+BaseActivity.email);

    }


    //터치로 이미지 넘기는 메서드 onTouch를 오버라이딩 해야함.
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (v != avf) return false;
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            xAtDown = event.getX();
        }
        //else if 부분이 중요함.
        else if (event.getAction() == MotionEvent.ACTION_UP) {
            xAtUp = event.getX();
            if (xAtDown > xAtUp) {
                avf.setInAnimation(v.getContext(), R.animator.right_in);
                avf.setOutAnimation(v.getContext(), R.animator.left_out);
                avf.showNext();
            } else if (xAtDown < xAtUp) {
                avf.setInAnimation(v.getContext(), R.animator.left_in);
                avf.setOutAnimation(v.getContext(), R.animator.right_out);
                avf.showPrevious();
                //다시 오른쪽->왼쪽으로 슬라이드 되도록 원상복구 시켜놓음.
                avf.setInAnimation(v.getContext(), R.animator.right_in);
                avf.setOutAnimation(v.getContext(), R.animator.left_out);
            }
        }
        return true;
    }


    @Override
    protected void onResume() {
        super.onResume();
       /* BaseActivity.mainList=new ArrayList<>();
        Log.d("응1","사이즈1"+String.valueOf(BaseActivity.mainList.size()));
        tempList = new ArrayList<>();
        Main_DataManager main_dataManager= new Main_DataManager();
        main_dataManager.loadData();
        Log.d("응1","사이즈2"+BaseActivity.mainList.size());
        for (int i = 0; i < BaseActivity.mainList.size(); i++) {
            tempList.add(BaseActivity.mainList.get(i));
            if (i == 6) {
                i = BaseActivity.mainList.size();
            }
        }
        Log.d("온리쥼"," 응");
       // Log.d("온리쥼"," "+tempList.get(0).getHit());
        //어뎁터 연결 ,널포인터 뜰 수 있음.
        avf = (AdapterViewFlipper) findViewById(R.id.main_AVF);
        avf.setAdapter(new galleryAdapter(this, tempList)); //갤러리 어뎁터에 연결
        avf.setOnTouchListener(this);//터치리스너에 연결*/



        navigation.setSelectedItemId(R.id.navigation_home);
    }

    public void setIntentFlag(Intent intent) {
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }

    ///////////////////////////////////////

    public class galleryAdapter extends BaseAdapter {
        private final Context mContext;
        LayoutInflater inflater;
        ArrayList<Model_Main> tempArr;


        public galleryAdapter(Context mContext, ArrayList<Model_Main> tempArr) {
            this.mContext = mContext;
            this.tempArr = tempArr;
            inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return tempList.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View converView, ViewGroup parent) {

            if (converView == null) {
                converView = inflater.inflate(R.layout.item_main, parent, false);
            }

            //정보 부분 클릭 시 (RelativeLayout클릭 시) DetailActivity로 인탠트
            mainRelativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    detail.putExtra("area", tempArr.get(position).getArea());
                    detail.putExtra("initials", tempArr.get(position).getInit());
                    detail.putExtra("name", tempArr.get(position).getName());
                    detail.putExtra("ename", tempArr.get(position).getEname());
                    detail.putExtra("lat", tempArr.get(position).getLat());
                    detail.putExtra("lng", tempArr.get(position).getLng());
                    detail.putExtra("subway", tempArr.get(position).getSubway());
                    detail.putExtra("bus", tempArr.get(position).getBus());
                    detail.putExtra("bicycle", tempArr.get(position).getBicycle());
                    detail.putExtra("url", tempArr.get(position).getUrl());
                    detail.putExtra("smartPhone", tempArr.get(position).getSmartPhone());
                    detail.putExtra("filter", tempArr.get(position).getFilter());
                    detail.putExtra("theme1", tempArr.get(position).getTheme1());
                    detail.putExtra("theme2", tempArr.get(position).getTheme2());
                    detail.putExtra("info", tempArr.get(position).getInfo());
                    detail.putExtra("tip", tempArr.get(position).getTip());
                    setIntentFlag(detail);
                    mContext.startActivity(detail);
                }
            });



            ImageView mimageView = (ImageView) converView.findViewById(R.id.mainImage);
            Picasso.with(mContext).load(tempArr.get(position).getUrl()).into(mimageView);

            for (int i = 0; i < tempArr.size(); i++) {
                String areaName = tempArr.get(i).getName();
                String areaEname = tempArr.get(i).getEname();
                String hits=tempArr.get(i).getHit();
                String theme=tempArr.get(i).getTheme1();
                if (i == position) {
                    nameEnglish.setText(areaEname);
                    nameKorean.setText(areaName);
                    hitTv.setText(hits);
                    themeTv.setText("# "+theme);
                }
            }
            //사진이 전환되면서 버튼 위 indexLight도 변하게끔 하는 부분.
            for (int i = 0; i < indexes.size(); i++) {
                ImageView index = indexes.get(i);
                if (i == position) {
                    index.setImageResource(R.drawable.indexafter);
                } else {
                    index.setImageResource(R.drawable.indexbefore);
                }
            }

            return converView;
        }

    }

}
