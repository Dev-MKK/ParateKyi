package mkk.myokoko.paratekyi;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import android.view.View.*; 

public class Parate { 
    private ArrayList<TextView> NoTvs = new ArrayList<>(); 
    private LinearLayout parateWrapper; 
    private ArrayList<TextView> Tvs = new ArrayList<>(); 
	private List<TextView> underTvs = new ArrayList<>();
    private int lineIndex = 0; 
    private Context mContext;  
    private boolean nextPara = true; 
    private int numberSize = 16; 
    private int paragraphIndex = 0; 
    private int parliSize = 16; 
	private KaraokePlayer mKaraokePlayer;

    public Parate(Context context, KaraokePlayer player) { 
        mContext = context; 
		mKaraokePlayer = player;
        init(); 
    } 
    private void addLines(String string2) { 
        String line = string2.replace("$",""); 
        LinearLayout lineWapper = new LinearLayout(mContext); 
        lineWapper.setOrientation(LinearLayout.HORIZONTAL);
        lineWapper.setLayoutParams((ViewGroup.LayoutParams)new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT)); 
        TextView numberTv = new TextView(this.mContext); 
        numberTv.setTextSize((float)this.numberSize); 
		numberTv.setTypeface(ParateActivity.typeface);
        numberTv.setGravity(Gravity.LEFT & Gravity.CENTER_VERTICAL); 
		numberTv.setPadding(10,10,0,10);
        numberTv.setMinimumWidth(dipToPixel(30)); 
        numberTv.setTextColor(Color.BLACK); 
        if (nextPara) { 
            paragraphIndex++; 
            numberTv.setText(toMyanmarDigit(paragraphIndex) + "။"); 
        } else { 
            numberTv.setText(" "); 
        } 
		
		LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
		RelativeLayout layout = new RelativeLayout(mContext);
		layout.setLayoutParams(params);
		
		TextView underTv = new TextView(mContext); 
        underTv.setTextSize((float)parliSize); 
		underTv.setTypeface(ParateActivity.typeface);
        underTv.setTextColor(Color.BLACK); 
		underTv.setPadding(10,10,10,10);
        String string4 = line.isEmpty() ? " " : line; 
        underTv.setText(string4); 
        underTv.setTag(lineIndex); 
		
        TextView parliTv = new TextView(mContext); 
        parliTv.setTextSize((float)parliSize); 
		parliTv.setTypeface(ParateActivity.typeface);
        parliTv.setTextColor(Color.BLACK); 
		parliTv.setPadding(10,10,10,10);
       // String string4 = line.isEmpty() ? " " : line; 
        parliTv.setText(string4); 
        parliTv.setTag(lineIndex); 
		
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT); 
        layoutParams.rightMargin = dipToPixel(8); 
        numberTv.setLayoutParams(layoutParams); 
		
        parliTv.setOnClickListener(new OnClickListener(){
				@Override
				public void onClick(View v)
				{
					// TODO: Implement this method
					int line = ((TextView)v).getTag();
					mKaraokePlayer.seekLine(line);
				}
			});
			
        nextPara = line.isEmpty(); 
        lineWapper.addView(numberTv); 
        lineWapper.addView(layout); 
		layout.addView(underTv);
		layout.addView(parliTv);
        parateWrapper.addView(lineWapper); 
        if (!line.isEmpty()) { 
            Tvs.add(parliTv); 
			underTvs.add(underTv);
            lineIndex++; 
        } 
        NoTvs.add(numberTv); 
    } 

    private int dipToPixel(int dip) { 
        return (int)(0.5 + (double)mContext.getResources().getDisplayMetrics().density * (double)dip); 
    } 

    private void init() { 
        parateWrapper = (LinearLayout)((Activity)mContext).findViewById(R.id.parateWrapper); 
    } 

    private String toMyanmarDigit(int digit) { 
        return ParateActivity.AMFD.writeZawgyi(
			("" + digit)
			.replace("0","၀").replace("5","၅")
			.replace("1","၁").replace("6","၆")
			.replace("2","၂").replace("7","၇")
			.replace("3","၃").replace("8","၈")
			.replace("4","၄").replace("9","၉")
		);
	}

    public void clearAndReset() { 
        if (parateWrapper.getChildCount() > 0) { 
            parateWrapper.removeAllViews(); 
        } 
        paragraphIndex = 0; 
        nextPara = true; 
        Tvs.clear(); 
        NoTvs.clear(); 
		underTvs.clear();
        lineIndex = 0; 
    } 

    public void decreaseTextsize() { 
        if (parliSize <= 16){
			return;
		}
        parliSize--; 
        Iterator iterator = Tvs.iterator(); 
        while (iterator.hasNext()) {
			((TextView)iterator.next()).setTextSize(parliSize);
		}
		Iterator iterator2 = underTvs.iterator(); 
        while (iterator2.hasNext()) {
			((TextView)iterator2.next()).setTextSize(parliSize);
		}
		if(numberSize <= 16) {
			return;
		}
		numberSize--;
		Iterator iterator3 = NoTvs.iterator(); 
        while (iterator3.hasNext()) {
			((TextView)iterator3.next()).setTextSize(numberSize);
		}
    } 

    public ArrayList<TextView> getTvs() { 
        return Tvs; 
    } 

    public void increaseTextsize() { 
        parliSize++; 
		Iterator iterator3 = underTvs.iterator(); 
        while (iterator3.hasNext()) {
			((TextView)iterator3.next()).setTextSize(parliSize);
		}
        Iterator iterator = Tvs.iterator(); 
        do { 
            if (!iterator.hasNext()) { 
                if (numberSize < 18) { 
                    numberSize++; 
                } 
                break; 
            } 
            ((TextView)iterator.next()).setTextSize(parliSize); 
        } while (true); 
        Iterator iterator2 = NoTvs.iterator(); 
        while (iterator2.hasNext()) { 
            ((TextView)iterator2.next()).setTextSize(numberSize); 
        } 
        return; 
    } 

    public void show(Collection arrayList) { 
        clearAndReset(); 
        Iterator iterator = arrayList.iterator(); 
        while (iterator.hasNext()) { 
            addLines((String)iterator.next()); 
        } 
        return; 
    } 
} 
 
 

