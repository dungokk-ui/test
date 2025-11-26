package com.example.gameduaxe;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    TextView tvScore1, tvScore2, tvScore3;
    ImageView img1, img2, img3;
    // ImageView finishLineView; // ĐÃ XÓA
    Button btnPlay;
    CheckBox cb1, cb2, cb3;

    // Bắt đầu với 10 điểm
    int score1 = 10, score2 = 10, score3 = 10;
    Handler handler = new Handler();
    Random random = new Random();

    // Sử dụng vạch đích cố định
    int finishLine = 900;

    // Biến để ngăn việc update điểm nhiều lần trong một cuộc đua
    boolean raceFinished = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvScore1 = findViewById(R.id.tvScore1);
        tvScore2 = findViewById(R.id.tvScore2);
        tvScore3 = findViewById(R.id.tvScore3);

        img1 = findViewById(R.id.image_goku);
        img2 = findViewById(R.id.image_vegeta);
        img3 = findViewById(R.id.img3);

        cb1 = findViewById(R.id.cb1);
        cb2 = findViewById(R.id.cb2);
        cb3 = findViewById(R.id.cb3);
        
        //inishLineView = findViewById(R.id.finish_line); // ĐÃ XÓA

        btnPlay = findViewById(R.id.btnPlay);
        
        // Cập nhật điểm số ban đầu
        tvScore1.setText(String.valueOf(score1));
        tvScore2.setText(String.valueOf(score2));
        tvScore3.setText(String.valueOf(score3));

        btnPlay.setOnClickListener(v -> {
            if (!cb1.isChecked() && !cb2.isChecked() && !cb3.isChecked()) {
                Toast.makeText(MainActivity.this, "Vui lòng đặt cược!", Toast.LENGTH_SHORT).show();
            } else {
                startRace();
            }
        });
    }

    void startRace() {
        // Đặt lại vị trí các nhân vật và trạng thái cuộc đua
        raceFinished = false;
        btnPlay.setEnabled(false); // Vô hiệu hóa nút Play khi đang đua
        cb1.setEnabled(false); // Vô hiệu hóa checkbox
        cb2.setEnabled(false);
        cb3.setEnabled(false);

        img1.setTranslationX(0);
        img2.setTranslationX(0);
        img3.setTranslationX(0);

        handler.post(new Runnable() {
            @Override
            public void run() {
                // Nếu cuộc đua đã kết thúc, không chạy nữa
                if (raceFinished) {
                    return;
                }
                
                // Di chuyển các nhân vật bằng translationX
                img1.setTranslationX(img1.getTranslationX() + random.nextInt(25));
                img2.setTranslationX(img2.getTranslationX() + random.nextInt(25));
                img3.setTranslationX(img3.getTranslationX() + random.nextInt(25));

                // Kiểm tra xem có nhân vật nào về đích chưa
                if (img1.getX() + img1.getWidth() >= finishLine ||
                    img2.getX() + img2.getWidth() >= finishLine ||
                    img3.getX() + img3.getWidth() >= finishLine) {
                    
                    raceFinished = true; // Đánh dấu cuộc đua đã kết thúc
                    updateScore();
                    btnPlay.setEnabled(true); // Bật lại nút Play
                    cb1.setEnabled(true); // Bật lại checkbox
                    cb2.setEnabled(true);
                    cb3.setEnabled(true);
                    return; // Dừng vòng lặp
                }

                handler.postDelayed(this, 30);
            }
        });
    }

    void updateScore() {
        // Tìm vị trí của người chiến thắng
        float x1 = img1.getX();
        float x2 = img2.getX();
        float x3 = img3.getX();

        float max = Math.max(x1, Math.max(x2, x3));

        // Chỉ cập nhật điểm cho các nhân vật được đặt cược
        if (x1 == max) { // Goku (img1) wins
            if (cb1.isChecked()) score1 += 10;
            if (cb2.isChecked()) score2 -= 5;
            if (cb3.isChecked()) score3 -= 5;
        } else if (x2 == max) { // Vegeta (img2) wins
            if (cb1.isChecked()) score1 -= 5;
            if (cb2.isChecked()) score2 += 10;
            if (cb3.isChecked()) score3 -= 5;
        } else { // Whis (img3) wins
            if (cb1.isChecked()) score1 -= 5;
            if (cb2.isChecked()) score2 -= 5;
            if (cb3.isChecked()) score3 += 10;
        }

        tvScore1.setText(String.valueOf(score1));
        tvScore2.setText(String.valueOf(score2));
        tvScore3.setText(String.valueOf(score3));
    }
}