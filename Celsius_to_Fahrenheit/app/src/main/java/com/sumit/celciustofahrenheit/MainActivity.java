package com.sumit.celciustofahrenheit;

import org.tensorflow.lite.Interpreter;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.AssetFileDescriptor;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MainActivity extends AppCompatActivity {
    EditText et;
    TextView hw;
    Interpreter tflite;
    public static String MODEL_FILENAME = "degree.tflite";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        et=findViewById(R.id.editbcx);
        hw =findViewById(R.id.hw);
        try {
            tflite = new Interpreter(loadModelFile());
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void opener(View view) {
        float prediction=doInference(et.getText().toString());
        hw.setText(Float.toString(prediction));
    }

    private float doInference(String inputString) {
        float[] inputVal=new float[1];
        inputVal[0]=Float.valueOf(inputString);
        float[][] output=new float[1][1];
        tflite.run(inputVal,output);
        float inferredValue=output[0][0];
        return  inferredValue;
    }

    private MappedByteBuffer loadModelFile() throws IOException{
        AssetFileDescriptor fileDescriptor=this.getAssets().openFd("degree.tflite");
        FileInputStream inputStream=new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel=inputStream.getChannel();
        long startOffset=fileDescriptor.getStartOffset();
        long declareLength=fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY,startOffset,declareLength);
    }
}
