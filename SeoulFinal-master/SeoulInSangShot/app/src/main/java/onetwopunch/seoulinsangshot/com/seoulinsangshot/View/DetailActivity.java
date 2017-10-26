package onetwopunch.seoulinsangshot.com.seoulinsangshot.View;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import onetwopunch.seoulinsangshot.com.seoulinsangshot.Controller.Adapter_Detail;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.DataManager.Best_DataManager;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.Model.Model_Best;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.Model.Model_Detail;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.Model.Model_ViewCount;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.R;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.View.Fragment.Fragment_Detail_Best;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.View.Fragment.Fragment_Detail_Comment;
import onetwopunch.seoulinsangshot.com.seoulinsangshot.View.Fragment.Fragment_Detail_Info;

public class DetailActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    Fragment_Detail_Info fragmentInfo;
    Fragment_Detail_Comment fragmentComment;
    Fragment_Detail_Best fragmentBest;

    Intent home;

    RecyclerView rv_detail;
    LinearLayoutManager manager;
    Adapter_Detail adapter;

    public static LinearLayout ll_capView;

    public static int viewCountInt;

    //우선은 Model_Detail에 String url; 만 넣고 설계함.
    public static List<Model_Detail> detailMainList;

    //뽐내기 사진용리스트
    public static ArrayList<Model_Best> bestList;
    //조회수용 리스트
    public static ArrayList<Model_ViewCount>viewList;
    //좋아요용 리스트
    //public static ArrayList<Model_LikeCount>likeList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
//        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#050518")));

        Drawable d = getResources().getDrawable(R.drawable.actionbar);
        getSupportActionBar().setBackgroundDrawable(d);

        ll_capView = (LinearLayout) findViewById(R.id.ll_capView);

        home = new Intent(this, MainActivity.class);
        //fragmen에 파라미터 넘겨주기 위해 번들 생성
        Bundle bundle = new Bundle();

        //뽐내기 사진을 위한 정의,호출부분.
        bestList=new ArrayList<Model_Best>();
        Best_DataManager bestDataManager= new Best_DataManager();
        bestDataManager.loadData();

        //조회수 다 읽어오기
        //viewList=new ArrayList<Model_ViewCount>();
        /*
        View_DataManager viewDataManager= new View_DataManager();
        viewDataManager.setData("joker1649",getIntent().getStringExtra("url"),getIntent().getStringExtra("area"));*/




        //ViewPager에 붙일 fragment 생성
        fragmentInfo = new Fragment_Detail_Info();

        fragmentComment = new Fragment_Detail_Comment();
        fragmentBest = new Fragment_Detail_Best();

        //fragment_Info 로 intent 받은 데이터 값 바로 put해주기.
        bundle.putString("area", getIntent().getStringExtra("area"));
        bundle.putString("initials", getIntent().getStringExtra("initials"));
        bundle.putString("name", getIntent().getStringExtra("name"));
        bundle.putString("ename", getIntent().getStringExtra("ename"));
        bundle.putString("lat", getIntent().getStringExtra("lat"));
        bundle.putString("lng", getIntent().getStringExtra("lng"));
        bundle.putString("subway", getIntent().getStringExtra("subway"));
        bundle.putString("bus", getIntent().getStringExtra("bus"));
        bundle.putString("bicycle", getIntent().getStringExtra("bicycle"));
        bundle.putString("url", getIntent().getStringExtra("url"));
        bundle.putString("smartPhone", getIntent().getStringExtra("smartPhone"));
        bundle.putString("filter", getIntent().getStringExtra("filter"));
        bundle.putString("theme1", getIntent().getStringExtra("theme1"));
        bundle.putString("theme2", getIntent().getStringExtra("theme2"));
        bundle.putString("time", getIntent().getStringExtra("time"));
        bundle.putString("tip", getIntent().getStringExtra("tip"));
        bundle.putString("info", getIntent().getStringExtra("info"));
        bundle.putString("viewCount",String.valueOf(viewCountInt));



        //fragment info,fragment Comment로 파라미터(bundle) 전송
        fragmentInfo.setArguments(bundle);
        fragmentComment.setArguments(bundle);
        fragmentBest.setArguments(bundle);


        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (ViewPager) findViewById(R.id.detail_container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.detail_tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.getTabAt(0).setIcon(R.drawable.infor);
        tabLayout.getTabAt(1).setIcon(R.drawable.ques);
        tabLayout.getTabAt(2).setIcon(R.drawable.best);

        //detailActivity에 붙을 리사이클러뷰 어댑터 연결.

        detailMainList = new ArrayList<>();
        for(int i=0;i<BaseActivity.testArr.size();i++)
        {
            if(BaseActivity.testArr.get(i).getInitials().equals(getIntent().getStringExtra("initials")))
            {
                detailMainList.add(new Model_Detail(BaseActivity.testArr.get(i).getUrl()));
            }
        }
        //detailMainList.add(new Model_Detail(getIntent().getStringExtra("url")));
        rv_detail = (RecyclerView) findViewById(R.id.rv_detail);
        manager = new LinearLayoutManager(getApplicationContext());
        manager.setOrientation(LinearLayoutManager.HORIZONTAL);
        adapter = new Adapter_Detail(getApplicationContext(), detailMainList);

        rv_detail.setLayoutManager(manager);
        rv_detail.setAdapter(adapter);


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_primary, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        setIntentFlag(home);
        startActivity(home);

        return super.onOptionsItemSelected(item);
    }

    public void setIntentFlag(Intent intent){
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position) {
                case 0:
                    return fragmentInfo;
                case 1:
                    return fragmentComment;
                case 2:
                    return fragmentBest;
                default:
                    return fragmentInfo;
            }
        }

        @Override
        public int getCount() {
            return 3;
        }

//        @Override
//        public CharSequence getPageTitle(int position) {
//            switch (position) {
//                case 0:
//                    return "정보";
//                case 1:
//                    return "댓글";
//                case 2:
//                    return "뽐내기";
//            }
//            return null;
//        }




    }

}

