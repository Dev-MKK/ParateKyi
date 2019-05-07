package mkk.myokoko.paratekyi;

import android.content.Context;
import android.content.res.AssetManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

public class FileReader {
    private ArrayList<String> lines = new ArrayList<>();
    private Context mContext;

    public FileReader(Context context) {
        mContext = context;
    }

    public ArrayList<String> readFile(int fileindex) throws IOException {
        lines.clear();
        BufferedReader br = new BufferedReader(new InputStreamReader(mContext.getAssets().open(
							(ParateActivity.AMFD.isZawgyi() ? "zawgyi" : "unicode")
							+ "/parate_" + fileindex))
						);
		String line;
        while((line = br.readLine()) != null) {
            lines.add(line.replace("$$","$ $"));
        }
		return lines;
    }
}

