package com.example.foodsaver;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class AnasayfaActivity extends BaseActivity {

    private static final String GROQ_API_KEY = "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";
    private static final String GROQ_API_URL = "https://api.groq.com/openai/v1/chat/completions";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anasayfa);
        setupBottomNav();
        setupNavigationDrawer(R.layout.activity_anasayfa);

        ImageButton robotButton = findViewById(R.id.chatButton);
        robotButton.setOnClickListener(view -> showChatDialog());



    }

    private void showChatDialog() {
        Dialog dialog = new Dialog(AnasayfaActivity.this);
        dialog.setContentView(R.layout.activity_chat_dialog);
        dialog.setCancelable(true);

        EditText inputText = dialog.findViewById(R.id.inputText);
        Button sendBtn = dialog.findViewById(R.id.sendBtn);
        LinearLayout chatContainer = dialog.findViewById(R.id.chatContainer);
        ScrollView scrollView = dialog.findViewById(R.id.scrollView);
        ImageButton closeButton = dialog.findViewById(R.id.closeButton);

        closeButton.setOnClickListener(v -> dialog.dismiss());

        addBotMessage("Merhaba! Ben akıllı robotunuz FoodGO! Size nasıl yardımcı olabilirim?", chatContainer, scrollView);

        sendBtn.setOnClickListener(view -> {
            String userMessage = inputText.getText().toString().trim();
            if (!userMessage.isEmpty()) {
                addUserMessage(userMessage, chatContainer, scrollView);
                inputText.setText("");
                sendToGroqAPI(userMessage, chatContainer, scrollView);
            }
        });

        dialog.show();
    }

    private void addUserMessage(String message, LinearLayout container, ScrollView scroll) {
        TextView textView = new TextView(this);
        textView.setText(message);
        textView.setTextSize(16);
        textView.setTextColor(0xFFFFFFFF);
        textView.setBackgroundColor(0xFF0084FF);
        textView.setPadding(24, 16, 24, 16);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.END;
        params.setMargins(50, 16, 16, 16);
        textView.setLayoutParams(params);

        container.addView(textView);
        scrollToBottom(scroll);
    }

    private void addBotMessage(String message, LinearLayout container, ScrollView scroll) {
        TextView textView = new TextView(this);
        textView.setText(message);
        textView.setTextSize(16);
        textView.setTextColor(0xFF000000);
        textView.setBackgroundColor(0xFFE5E5EA);
        textView.setPadding(24, 16, 24, 16);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.START;
        params.setMargins(16, 16, 50, 16);
        textView.setLayoutParams(params);

        container.addView(textView);
        scrollToBottom(scroll);
    }

    private void scrollToBottom(ScrollView scroll) {
        scroll.post(() -> scroll.fullScroll(View.FOCUS_DOWN));
    }

    private void sendToGroqAPI(String userMessage, LinearLayout container, ScrollView scroll) {
        OkHttpClient client = new OkHttpClient();

        try {
            JSONArray messages = new JSONArray();


            JSONObject systemObj = new JSONObject();
            systemObj.put("role", "system");
            systemObj.put("content", "Sen yalnızca yemek tarifleri, gıda saklama yöntemleri, gıda israfını önleme yolları ve mutfak ipuçları hakkında konuşabilirsin. " +
                    "Diğer konularda sorulara cevap verme. Eğer bir kullanıcı farklı bir konuda soru sorarsa, şu cevabı ver: " +
                    "'Üzgünüm, yalnızca yemekle ilgili konularda yardımcı olabilirim.'");
            messages.put(systemObj);


            JSONObject userObj = new JSONObject();
            userObj.put("role", "user");
            userObj.put("content", userMessage);
            messages.put(userObj);

            JSONObject jsonBody = new JSONObject();
            jsonBody.put("model", "llama-3.3-70b-versatile"); // veya Groq'taki uygun modelin
            jsonBody.put("messages", messages);
            jsonBody.put("temperature", 0.7);

            RequestBody body = RequestBody.create(
                    jsonBody.toString(),
                    MediaType.parse("application/json")
            );

            Request request = new Request.Builder()
                    .url(GROQ_API_URL)
                    .addHeader("Authorization", "Bearer " + GROQ_API_KEY)
                    .addHeader("Content-Type", "application/json")
                    .post(body)
                    .build();

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(() -> addBotMessage("Bağlantı hatası: " + e.getMessage(), container, scroll));
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    try (ResponseBody responseBody = response.body()) {
                        if (!response.isSuccessful()) {
                            runOnUiThread(() -> addBotMessage("API hatası: " + response.code(), container, scroll));
                            return;
                        }

                        String responseStr = responseBody != null ? responseBody.string() : "";
                        JSONObject jsonResponse = new JSONObject(responseStr);
                        JSONArray choices = jsonResponse.getJSONArray("choices");
                        String reply = choices.getJSONObject(0).getJSONObject("message").getString("content");

                        runOnUiThread(() -> addBotMessage(reply.trim(), container, scroll));
                    } catch (Exception e) {
                        runOnUiThread(() -> addBotMessage("Cevap işleme hatası: " + e.getMessage(), container, scroll));
                    }
                }
            });

        } catch (Exception e) {
            addBotMessage("JSON hatası: " + e.getMessage(), container, scroll);
        }
    }
}
