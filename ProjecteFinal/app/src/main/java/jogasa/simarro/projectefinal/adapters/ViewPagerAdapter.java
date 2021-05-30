package jogasa.simarro.projectefinal.adapters;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.google.common.io.Resources;

import jogasa.simarro.projectefinal.R;


public class ViewPagerAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater inflater;
    private  String[] titles;
    private String descs[];
    public ViewPagerAdapter(Context context) {
        this.context = context;
        this.titles= new String[]{
                context.getResources().getString(R.string.seller),
                context.getResources().getString(R.string.client),
        };
        this.descs=new String[]{
                context.getResources().getString(R.string.uploadDesc)+"\n"+context.getResources().getString(R.string.manageOrdersDesc),
                context.getResources().getString(R.string.buyDesc)+"\n"+context.getResources().getString(R.string.orderHistoryDesc),
        };
    }

    private int[] images ={
            R.drawable.vendor,
            R.drawable.customer,
    };

    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (LinearLayout)object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        inflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.activity_view_pager_adapter,container,false);

        //init views
        ImageView imageView = v.findViewById(R.id.imgViewPager);
        TextView txtTitle = v.findViewById(R.id.txtTitleViewPager);
        TextView txtDesc = v.findViewById(R.id.txtDescViewPager);

        imageView.setImageResource(images[position]);
        txtTitle.setText(titles[position]);
        txtDesc.setText(descs[position]);

        container.addView(v);
        return v;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((LinearLayout)object);
    }
}
