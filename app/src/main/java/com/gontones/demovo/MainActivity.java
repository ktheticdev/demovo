package com.gontones.demovo;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.util.Base64;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import static com.gontones.demovo.FirstRun.isFirstRun;


public class MainActivity extends Activity implements View.OnClickListener {
    static String gotv;
    static String text = new String("UTF-8");
    static String aescrypted;
    static String ciphered;
    static String decodeone;
    static String decodetwo;
    static String original;
    static long sdvig;
    static String keytoCipher;
    static SharedPreferences prefs;
    static String result;
    static String presdvig;
    static String presalt;
    EditText etNum1;
    Context context = this;
    Button btnAdd;
    Button btnSub;
    static String initvector = "defaultvector123";
    static int style;
    static boolean first;

    TextView tvResult;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        prefs = PreferenceManager.getDefaultSharedPreferences(this);
        style = Integer.parseInt(prefs.getString("style", "1"));
        try {
            if (style == 1) {
                setTheme(R.style.AppTheme);
            } else if (style == 2) {
                setTheme(R.style.AppTheme_Light);
            } else if (style == 3) {
                setTheme(R.style.Sys);
            } else if (style == 4) {
                setTheme(R.style.Sys_Light);
            } else if (style == 5) {
                setTheme(R.style.Holo);
            } else if (style == 6) {
                setTheme(R.style.Holo_Light);
            } else if (style == 7) {
                setTheme(R.style.Necro);
            } else if (style == 8) {
                setTheme(R.style.Necro_Light);
            }
        } catch (Exception themed) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                setTheme(R.style.Holo);
                SharedPreferences.Editor edit = prefs.edit();
                edit.putString("style", "5");
                edit.commit();
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setTheme(R.style.AppTheme);
                SharedPreferences.Editor edit = prefs.edit();
                edit.putString("style", "1");
                edit.commit();
            } else {
                setTheme(R.style.Necro);
                SharedPreferences.Editor edit = prefs.edit();
                edit.putString("style", "7");
                edit.commit();
            }
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (isFirstRun(this)) {
            Intent firstlaunch = new Intent(this, SettingsActivity.class);
            startActivity(firstlaunch);
            first = true;
        }
        etNum1 = (EditText) findViewById(R.id.etNum1);

        btnAdd = (Button) findViewById(R.id.btnAdd);
        btnSub = (Button) findViewById(R.id.btnSub);

        tvResult = (TextView) findViewById(R.id.tvResult);

        btnAdd.setOnClickListener(this);
        btnSub.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        text = etNum1.getText().toString();

        switch (v.getId()) {
            case R.id.btnAdd:
                prefs = PreferenceManager.getDefaultSharedPreferences(this);
                presdvig = prefs.getString("firstkey", presdvig);
                presalt = prefs.getString("salt", "defaultsalt");
                keytoCipher = prefs.getString("secondkey", keytoCipher);

                try {
                    caesarEncode();
                    precipher();
                    base64code();
                    boolean format = prefs.getBoolean("format", false);
                    if (format == false) {
                        ciphered = ciphered.replace("\n", "");
                        result = ciphered;
                    }
                    if (format == true) {
                        ciphered = "`" + ciphered + "`";
                        ciphered = ciphered.replace("\n", "");
                        result = ciphered;
                    }
                } catch (Exception e) {
                        etNum1.setText("");
                        result = getString(R.string.errorcode);
                        e.printStackTrace();
                }
                break;
            case R.id.btnSub:
                prefs = PreferenceManager.getDefaultSharedPreferences(this);
                presdvig = prefs.getString("firstkey", presdvig);
                presalt = prefs.getString("salt", "defaultsalt");
                keytoCipher = prefs.getString("secondkey", keytoCipher);
                try {
                    base64decode();
                    decipher();
                    caesarDecode();
                    result = original;
                } catch (Exception e) {
                    try {
                        text = text.replace("`", "");
                        base64decode();
                        decipher();
                        caesarDecode();
                        result = original;
                    } catch (Exception e1) {
                        result = getString(R.string.errordecode);
                    }
                }
                break;
            default:
                break;
        }
        tvResult.setText(result);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item0:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                    ClipboardManager clipboard = null;
                    clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = null;
                    clip = ClipData.newPlainText("", tvResult.getText().toString());
                    clipboard.setPrimaryClip(clip);
                } else if(Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES. HONEYCOMB) {
                    android.text.ClipboardManager clipboard = (android.text.ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    clipboard.setText(tvResult.getText().toString());
                }
                return true;
            case R.id.item1:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                return true;
            case R.id.item2:
                etNum1.setText("");
                tvResult.setText("");
                return true;
            case R.id.item3:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static void caesarEncode() {
        char sym = 'x';
        sdvig = Long.parseLong(presdvig);
        int x = 0;
        gotv = "";
        while (x != text.length()) {
            sym = text.charAt(x);
            long caesar = (long) sym + sdvig;
            gotv = gotv + (char) caesar;
            ++x;
        }
    }

    public static void precipher() {
        String salt = md5Custom(presalt);
        TextEncryptor encryptor = Encryptors.text(keytoCipher, salt);
        aescrypted = encryptor.encrypt(gotv);
    }

    public static void decipher() {
        String salt = md5Custom(presalt);
        TextEncryptor encryptor = Encryptors.text(keytoCipher, salt);
        decodetwo = encryptor.decrypt(decodeone);
    }

    public static String md5Custom(String st) {
        MessageDigest messageDigest = null;
        byte[] digest = new byte[0];

        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.reset();
            messageDigest.update(st.getBytes());
            digest = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        BigInteger bigInt = new BigInteger(1, digest);
        String md5Hex = bigInt.toString(16);
        while (md5Hex.length() < 32) {
            md5Hex = "0" + md5Hex;
        }
        return md5Hex;
    }


    public static void caesarDecode() {
        char sym = 'x';
        sdvig = Long.parseLong(presdvig);
        int x = 0;
        original = "";
        while (x != decodetwo.length()) {
            sym = decodetwo.charAt(x);
            long caesar = (long) sym - sdvig;
            original = original + (char) caesar;
            ++x;
        }
    }

    public static void base64code() {
        byte[] gontocrypt = aescrypted.getBytes();
        ciphered = Base64.encodeToString(gontocrypt, Base64.DEFAULT);
        byte[] bytearr = ciphered.getBytes();
        ciphered = new String(bytearr);

    }

    public static void base64decode() {
        byte[] ok = Base64.decode(text, Base64.DEFAULT);
        decodeone = new String(ok);
    }
}
