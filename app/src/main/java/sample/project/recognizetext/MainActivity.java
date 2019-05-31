package sample.project.recognizetext;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.common.FirebaseVisionImageMetadata;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextRecognizer;

import java.util.List;
import java.util.concurrent.LinkedBlockingDeque;

public class MainActivity extends AppCompatActivity {


    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final int[] img_id = new int[]{
                R.drawable.abc,
                R.drawable.number,
                R.drawable.download,
                R.drawable.images,
                R.drawable.summa
        };

        final ImageSwitcher switcher = findViewById(R.id.image_switch);
        switcher.setFactory(new ViewSwitcher.ViewFactory() {
            @Override
            public View makeView() {
                ImageView view = new ImageView(getApplicationContext());
                view.setLayoutParams(new
                        ImageSwitcher.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT));
                return view;
            }
        });

        switcher.setInAnimation(this, android.R.anim.slide_in_left);
        switcher.setOutAnimation(this, android.R.anim.slide_out_right);

        findViewById(R.id.buttonnext).setOnClickListener(new View.OnClickListener() {
            private int pos = 0;

            @Override
            public void onClick(View v) {
                if(pos == img_id.length)
                    pos = 0;
                switcher.setImageResource(img_id[pos]);
                Bitmap icon = BitmapFactory.decodeResource(getResources(),img_id[pos]);
                runTextRecognition(icon);
                textView.setText("");
                pos++;
            }
        });

        textView = findViewById(R.id.textview);

    }

    private void runTextRecognition(final Bitmap bitmap) {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(bitmap);
        FirebaseVisionTextRecognizer recognizer = FirebaseVision.getInstance()
                .getOnDeviceTextRecognizer();

        recognizer.processImage(image)
                .addOnSuccessListener(
                        new OnSuccessListener<FirebaseVisionText>() {
                            @Override
                            public void onSuccess(FirebaseVisionText texts) {
//                                int block_count = 1;
                                int line_count = 1;
                                //processTextRecognitionResult(texts);
//                                for (FirebaseVisionText.TextBlock block : texts.getTextBlocks()){
//                                    textView.append("Block " + block_count + ": " + block.getText() + "\n\n");
//                                    block_count++;
//                                }

                                for (FirebaseVisionText.TextBlock block : texts.getTextBlocks()){
                                    for(FirebaseVisionText.Line line : block.getLines()){
                                        textView.append("Line no " + line_count + ":" + line.getText() + "\n\n");
                                        line_count++;
                                    }
                                }
                            }
                        })
                .addOnFailureListener(
                        new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                // Task failed with an exception
                                e.printStackTrace();
                            }
                        });
    }

    /*private void processTextRecognitionResult(FirebaseVisionText texts) {
        List<FirebaseVisionText.TextBlock> blocks = texts.getTextBlocks();
        if (blocks.size() == 0) {
            Toast.makeText(MainActivity.this, "NO TEXT", Toast.LENGTH_SHORT).show();
            return;
        }
        for (int i = 0; i < blocks.size(); i++) {
            List<FirebaseVisionText.Line> lines = blocks.get(i).getLines();
            for (int j = 0; j < lines.size(); j++) {
                List<FirebaseVisionText.Element> elements = lines.get(j).getElements();
                for (int k = 0; k < elements.size(); k++) {
                    textView.setText(elements.get(k).toString());
                    Log.d("DATA", elements.get(k).toString());
                }
            }
        }
    }*/
}
