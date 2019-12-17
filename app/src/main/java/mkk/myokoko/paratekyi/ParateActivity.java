package mkk.myokoko.paratekyi;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import java.io.*;

public class ParateActivity extends Activity 
implements ContentsBuilder.ContentsBuilderClickListener {
	
	static AutoMyanmarFontDetector AMFD;
    static Typeface typeface;
    ContentsBuilder CB;
    private Parate Parate;
    TextView contentsTv;
    private Handler h = new Handler();
    TextView headerTv;
    private KaraokePlayer mKaraokePlayer;
    private boolean needTip = true;
    TextView splashBottomTv;
    TextView splashSmallTv;
    TextView splashTv;
	
	

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

		AMFD = new AutoMyanmarFontDetector(this,findViewById(R.id.root));
        typeface = AMFD.getTypeface();
        contentsTv = (TextView)findViewById(R.id.contents_tv);
        headerTv = (TextView)findViewById(R.id.header);
        splashSmallTv = (TextView)findViewById(R.id.splash_small_tv);
        splashSmallTv.setTypeface(typeface);
        splashSmallTv.setText(toMyanmar(" ပါဠိ - အသံ "));
        contentsTv.setTypeface(typeface);
        headerTv.setTypeface(typeface);
		headerTv.setKeepScreenOn(true);
        splashTv = (TextView)findViewById(R.id.splashtv);
        splashTv.setTypeface(typeface);
        splashTv.setText(toMyanmar("ပရိတ္ၾကီး ဝတ္ရြတ္စဥ္"));
        splashBottomTv = (TextView)findViewById(R.id.splash_bottom_tv);
        splashBottomTv.setTypeface(typeface);
        splashBottomTv.setText(toMyanmar("ကိုမ်ိဳး မွ ေရးသားပူေဇာ္သည္။"));
        mKaraokePlayer = new KaraokePlayer(this);
        Parate = new Parate(this,mKaraokePlayer);
        h.postDelayed(mRunnable,5500);
    }
	
	private void changeHeader(String title) {
        headerTv.setText(title);
    }

    private void createContents() {
		contentsTv.setVisibility(View.VISIBLE);
        handleBottomButtons(false);
        changeHeader(AMFD.writeUnicode("မာတိကာ"));
        isTape(false);
        shutDownKaraokePlayer();
        CB = new ContentsBuilder(this).makeTitles(
				AMFD.writeUnicode("ပရိတ်နိဒါန်း"),
				AMFD.writeUnicode("မင်္ဂလသုတ်"),
				AMFD.writeUnicode("ရတနသုတ်"),
				AMFD.writeUnicode("​​မေတ္တသုတ်"),
				AMFD.writeUnicode("ခန္ဓသုတ်"),
				AMFD.writeUnicode("​မောရသုတ်"),
				AMFD.writeUnicode("ဝဋ္ဋသုတ်"),
				AMFD.writeUnicode("ဓဇဂ္ဂသုတ်"),
				AMFD.writeUnicode("အာဋာနာဋိယသုတ်"),
				AMFD.writeUnicode("အင်္ဂုလိမာလသုတ်"),
				AMFD.writeUnicode("ဗောဇ္ဈင်္ဂသုတ်"),
				AMFD.writeUnicode("ပုဗ္ဗဏှသုတ်")
				)
				.showAt(contentsTv);
        toParateToast("မိမိ ပူေဇာ္လိုေသာ သုတ္ကို လက္ျဖင့္ ေထာက္ဖြင့္ပါ။");
    }

    private void delaySplashScreen() {
        createContents();
        findViewById(R.id.splash).setVisibility(View.GONE);
    }

    private int dipToPixel(int dip) {
        return (int)(0.5 + (double)getResources().getDisplayMetrics().density * (double)dip);
    }

    private void handleBottomButtons(boolean bl) {
        if (bl) {
            findViewById(R.id.contentsBtn).setVisibility(View.VISIBLE);
            findViewById(R.id.tape).setVisibility(View.VISIBLE);
            findViewById(R.id.textsizeminus).setVisibility(View.VISIBLE);																																																																	
            findViewById(R.id.textsizeplus).setVisibility(View.VISIBLE);
            return;
        }
        findViewById(R.id.contentsBtn).setVisibility(View.GONE);
		findViewById(R.id.tape).setVisibility(View.GONE);
		findViewById(R.id.textsizeminus).setVisibility(View.GONE);
		findViewById(R.id.textsizeplus).setVisibility(View.GONE);
		
    }

    private void isTape(boolean play) {
        if (play) {
            findViewById(R.id.tape).setBackgroundResource(R.drawable.tape_on);
            return;
        }
        findViewById(R.id.tape).setBackgroundResource(R.drawable.tape_off);
    }

    private void shutDownKaraokePlayer() {
        if (mKaraokePlayer != null) {
            mKaraokePlayer.shutDown();
        }
    }

    private String toMyanmar(String text) {
        return AMFD.writeZawgyi(text);
    }

    private void toParateToast(String msg) {
        if (!needTip) {
            return;
        }
        View view = getLayoutInflater().inflate(R.layout.toast, (ViewGroup)findViewById(R.id.twrapper));
        view.setBackgroundResource(R.drawable.splash_bg);
        ((ImageView)view.findViewById(R.id.timage)).setBackgroundResource(R.drawable.circle);
		((TextView)view.findViewById(R.id.ttitle)).setTypeface(typeface);
        ((TextView)view.findViewById(R.id.ttitle)).setText(toMyanmar(msg));
        Toast toast = new Toast(this);
        toast.setGravity(Gravity.LEFT, dipToPixel(5), dipToPixel(100));
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(view);
        toast.show();
    }

    @Override
    public void OnContentTitleClick(int index) {
        try {
            ArrayList<String> list = new FileReader(this).readFile(index);
            Parate.show(list);
            mKaraokePlayer.locateKaraokeTextViews(Parate.getTvs());
            mKaraokePlayer.loadKaraokeFiles(index,list);
            contentsTv.setVisibility(View.GONE);
			
        }
        catch (Exception e) {
            handleBottomButtons(false);
            isTape(false);
			Toast.makeText(this,e.getMessage(),Toast.LENGTH_LONG).show();
            return;
        } 
        changeHeader(CB.getTitle(index));
        handleBottomButtons(true);
        isTape(true);
        toParateToast("ပါဠိစာေၾကာင္းေပၚကို လက္ျဖင့္ေထာက္၍လည္း ပူေဇာ္ႏိုင္ပါသည္။");
        needTip = false;
    }

    public void handleTape(View view) {
        mKaraokePlayer.play();
        isTape(mKaraokePlayer.isPlaying());
    }

    public void minusTextsize(View view) {
        Parate.decreaseTextsize();
    }

    @Override
    public void onBackPressed() {
        mKaraokePlayer.shutDown();
        super.onBackPressed();
    }
	
	@Override
    protected void onDestroy() {
       	mKaraokePlayer.shutDown();
        super.onDestroy();
    }

    public void plusTextsize(View view) {
        Parate.increaseTextsize();
    }

	Runnable mRunnable = new Runnable(){
		@Override
		public void run()
		{
			// TODO: Implement this method
			delaySplashScreen();
		}
	};
    public void showContents(View view) {
        createContents();
        Parate.clearAndReset();
    }

  
}
