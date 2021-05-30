package jogasa.simarro.projectefinal.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import jogasa.simarro.projectefinal.R;
import jogasa.simarro.projectefinal.adapters.ViewPagerAdapter;

public class OnBoardActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private Button btnLeft,btnRight;
    private ViewPagerAdapter adapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_board);
        init();
    }

    private void init() {
        viewPager = findViewById(R.id.view_pager);
        btnLeft = findViewById(R.id.btnLeft);
        btnRight = findViewById(R.id.btnRight);
        dotsLayout = findViewById(R.id.dotsLayout);
        adapter = new ViewPagerAdapter(this);
        addDots(0);
        viewPager.addOnPageChangeListener(listener); // create this listener
        viewPager.setAdapter(adapter);

        btnRight.setOnClickListener(v->{
            //if button text is next we will go to next page of viewpager
            if (btnRight.getText().toString().equals(getResources().getString(R.string.choose))){
                Intent intent=new Intent(OnBoardActivity.this, SignUpActivity.class);
                if(viewPager.getCurrentItem()==0) intent.putExtra("Option","seller");
                if(viewPager.getCurrentItem()==1) intent.putExtra("Option","client");
                startActivity(intent);
            }
        });

        btnLeft.setOnClickListener(v->{
            // if btn skip clicked then we go to page 3
            Intent intent=new Intent(OnBoardActivity.this, LoginActivity.class);
            startActivity(intent);
        });


    }

    //method to create dots from html code
    private void addDots(int position){
        dotsLayout.removeAllViews();
        dots = new TextView[2];
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            //this html code creates dot
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.darkgrey2));
            dotsLayout.addView(dots[i]);
        }

        // ok now lets change the selected dot color
        if(dots.length>0){
            dots[position].setTextColor(getResources().getColor(R.color.black));
        }
    }

    private ViewPager.OnPageChangeListener listener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDots(position);
            //ok now we need to change the text of Next button to Finish if we reached page 3
            //and hide Skip button if we are not in page 1
            btnLeft.setVisibility(View.VISIBLE);
            btnLeft.setEnabled(true);
            btnRight.setText(R.string.choose);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}