package com.example.orderfood;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.orderfood.Database.MyDataBase;

import java.util.Properties;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
public class ForgotPassword extends AppCompatActivity {
    private EditText edtEmail;
    private Button submitButton;
    private MyDataBase myDatabase;

    private static final String EMAIL = "nguyendinhtu11022002@gmail.com";
    private static final String PASSWORD = "kttcaovurwsbmaoj";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pw);
        myDatabase = new MyDataBase(this);
        edtEmail = findViewById(R.id.edtEmail);
        submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = edtEmail.getText().toString();

                if (!isValidEmail(email)) {
                    Toast.makeText(ForgotPassword.this, "Invalid email address", Toast.LENGTH_SHORT).show();
                } else {
                    String password = myDatabase.getPasswordFromDatabase(email);

                    if (password != null) {
                        sendEmail(email, password); // Gửi email khi tìm thấy mật khẩu
                    } else {
                        Toast.makeText(ForgotPassword.this, "Password not found", Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });
    }
    private boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
        return Pattern.matches(emailPattern, email);
    }
    private void sendEmail(final String email, final String password) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Cấu hình thông tin email
                    Properties properties = new Properties();
                    properties.put("mail.smtp.auth", "true");
                    properties.put("mail.smtp.starttls.enable", "true");
                    properties.put("mail.smtp.host", "smtp.gmail.com");
                    properties.put("mail.smtp.port", "587");

                    // Tạo session
                    Session session = Session.getInstance(properties, new Authenticator() {
                        @Override
                        protected PasswordAuthentication getPasswordAuthentication() {
                            return new PasswordAuthentication(EMAIL, PASSWORD);
                        }
                    });

                    // Tạo đối tượng MimeMessage
                    MimeMessage message = new MimeMessage(session);

                    // Đặt thông tin người gửi
                    message.setFrom(new InternetAddress(EMAIL));

                    // Đặt thông tin người nhận
                    message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));

                    // Đặt chủ đề email
                    message.setSubject("Forgot Password - OrderFood App");

                    // Đặt nội dung email
                    String emailContent = "Your password is: " + password;
                    message.setText(emailContent);

                    // Gửi email
                    Transport.send(message);

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Hiển thị thông báo gửi email thành công
                            Toast.makeText(ForgotPassword.this, "Password sent to your email", Toast.LENGTH_SHORT).show();
                        }
                    });
                } catch (final MessagingException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // Hiển thị thông báo gửi email thất bại
                            Log.e("Loi" , e.getMessage());
                            Toast.makeText(ForgotPassword.this, "Failed to send password: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        thread.start();
    }
}
