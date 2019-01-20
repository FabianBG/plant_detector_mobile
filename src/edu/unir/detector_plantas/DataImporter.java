package edu.unir.detector_plantas;

import android.content.Context;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import edu.unir.detector_plantas.env.Logger;

public class DataImporter {
    private Logger Log = new Logger();
    private String filename = "file://android_asset/data.txt";
    private Map<String, String> data;

    private int LINE_LENGTH = 50;

    public String[] getKeyValue(String key){
        String desc = this.data.get(key);
        if(desc == null) return new String[] {};
        int lines = (desc.length() / LINE_LENGTH);
        int slope = desc.length() % LINE_LENGTH;
        String[] result;
        if(slope > 0){
            slope = desc.length() - (lines * LINE_LENGTH) - 1;
            result = new String[lines+2];
            result[lines+1] = desc.substring(lines * LINE_LENGTH, desc.length()-1);
        }else
            result = new String[lines+1];

        result[0] = key.toUpperCase() + ":";
        for (int i=0; i<lines; i++){
            result[i+1] = desc.substring(i*LINE_LENGTH, i*LINE_LENGTH + LINE_LENGTH);
            Log.i("LINESTRING"  +  result[i+1]);
        }

        return result;
    }


    public void readFile(Context context){
        data = new HashMap<>();
        try {
            InputStreamReader is = new InputStreamReader(context.getResources().getAssets().open("data.txt"));
            BufferedReader input =  new BufferedReader(is, 1024*8);
            try {
                String line = null;
                while (( line = input.readLine()) != null){
                    String [] splitedData = this.formatLine(line);
                    Log.i(splitedData[1]);
                    data.put(splitedData[0], splitedData[1]);
                }
            }
            finally {
                input.close();
            }
        }
        catch (FileNotFoundException ex) {
            Log.e("Couldn't find the file " + ex);
        }
        catch (IOException ex){
            Log.e("Error reading file " + ex);
        }

    }

    private String[] formatLine(String line){
        return line.split(";");
    }
}
