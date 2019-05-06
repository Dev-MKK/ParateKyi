
package mkk.myokoko.paratekyi;


import android.graphics.*;
import android.text.*;
import android.text.style.*;
import android.view.*;
import android.widget.*;
import java.util.*;
import android.text.method.*;

public class ContentsBuilder {
    private StringBuilder body = new StringBuilder();
    protected ContentsBuilderClickListener mListener;
    private ArrayList<WordAndColor> mData;
    private TextView mTextView;
    private ArrayList<String> mTitles = new ArrayList<>();

    public ContentsBuilder(ContentsBuilderClickListener contentsBuilderClickListener) {
        mListener = contentsBuilderClickListener;
        gettingReady();
    }

    private void gettingReady() {
        mData = new ArrayList<>();
    }

    private void makeSpannedText() {
        Spannable wordToSpan = new SpannableString(body);

		int index = 0;
		for(WordAndColor wc: mData) {
			String keyword = wc.word;
			int color = wc.color;

			int ofe = body.indexOf(keyword,0);   
			for(int ofs = 0; ofs < body.length() && ofe != -1; ofs = ofe + 1) {       
				ofe = body.indexOf(keyword,ofs);   
				if(ofe == -1) {
					break;
				} else { 
					wordToSpan.setSpan(new ForegroundColorSpan(color) , ofe, ofe + keyword.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
					wordToSpan.setSpan(new Click(index), ofe, ofe + keyword.length(),Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				}
			}
			index++;
		}  
		mTextView.setText(wordToSpan, TextView.BufferType.SPANNABLE);
		mTextView.setMovementMethod(new LinkMovementMethod());
    }

    private void show() {
        makeSpannedText();
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

    private String toMyanmar(String text) {
        return ParateActivity.AMFD.writeZawgyi(text);
    }

    public String getTitle(int n) {
        return mTitles.get(n - 1);
    }

    public ContentsBuilder makeTitles(String ... titles) {
        int no = 1;
        body.append("\n");
        mTitles.clear();
        int i = 0;
        while (i < titles.length) {
            String title = toMyanmar(titles[i]);
            body.append(toMyanmarDigit(no))
			.append("။   ")
			.append(title)
			.append("\n\n");
            mData.add(new WordAndColor(title, Color.BLACK));
            mTitles.add(title);
            ++no;
            ++i;
        }
        return this;
    }

    public ContentsBuilder showAt(TextView textView) {
        this.mTextView = textView;
        show();
        return this;
    }

    private class Click extends ClickableSpan {
        private int index;
        
        public Click(int n) {
            index = n;
        }

        public void onClick(View view) {
            mListener.OnContentTitleClick(1 + index);
        }

        public void updateDrawState(TextPaint textPaint) {
            textPaint.setUnderlineText(false);
        }
    }

    public static interface ContentsBuilderClickListener {
        public void OnContentTitleClick(int index);
    }

    private class WordAndColor {
        public int color;
        public String word;

        public WordAndColor(String word, int color) {
            this.word = word;
            this.color = color;
        }
    }

}

