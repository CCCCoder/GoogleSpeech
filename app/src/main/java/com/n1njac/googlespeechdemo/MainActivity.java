package com.n1njac.googlespeechdemo;

import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.voicesearch.GservicesHelper;
import com.google.android.voicesearch.VoiceSearchApplication;
import com.google.android.voicesearch.VoiceSearchContainer;
import com.google.android.voicesearch.actions.VoiceAction;
import com.google.android.voicesearch.speechservice.RecognitionController;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    private RecognitionController mController;
    private RecognitionListener mCallback;
    private Button mStartBtn, mStopBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mStartBtn = (Button) findViewById(R.id.start_btn);
        mStopBtn = (Button) findViewById(R.id.stop_btn);


        VoiceSearchContainer localVoiceSearchContainer = VoiceSearchApplication.getContainer(this);
        this.mController = localVoiceSearchContainer.createRecognitionController();

        GservicesHelper gServicesHelper = localVoiceSearchContainer.getGservicesHelper();
        this.mCallback = new VoiceSearchRecognitionListener();

        //参数可以在对应的RecognizerIntent类中找到，其实这里的用法个SpeechRecognizer是一样的，可以去看下这个类的用法。
        final Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra("fullRecognitionResultsRequest", true);
        intent.putExtra("calling_package", "android");
        intent.putExtra("contact_auth", true);
        intent.putExtra("useLocation", true);
        intent.putExtra("ptt", 0);
        intent.putExtra("android.speech.extras.SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS", gServicesHelper.getEndpointerCompleteSilenceMillis());
        intent.putExtra("android.speech.extras.SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS", gServicesHelper.getEndpointerPossiblyCompleteSilenceMillis());

        mStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mController.onStartListening(intent, mCallback);
            }
        });
        mStopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mController.onStopListening(mCallback);
            }
        });
    }

    private class VoiceSearchRecognitionListener implements RecognitionListener {
        private VoiceSearchRecognitionListener() {
        }

        public void onBeginningOfSpeech() {

            Log.i("xyz", "---------->onBeginningOfSpeech");

        }

        public void onBufferReceived(byte[] paramArrayOfByte) {

        }

        public void onEndOfSpeech() {

            Log.i("xyz", "---------->onEndOfSpeech");
        }

        public void onError(int paramInt) {

            Log.i("xyz", "---------->onError:" + paramInt);

        }

        public void onEvent(int paramInt, Bundle paramBundle) {


        }

        public void onPartialResults(Bundle paramBundle) {
            Log.i("xyz", "---------->onPartialResults");
            Log.i("xyz", "onPartialResults:" + paramBundle.toString());
        }

        public void onReadyForSpeech(Bundle paramBundle) {

            Log.i("xyz", "---------->onReadyForSpeech");
        }

        public void onResults(Bundle paramBundle) {
            Log.i("xyz", "---------->onResults");

            Log.i("xyz", "onResults:" + paramBundle.toString());

            ArrayList<VoiceAction> localArrayList = paramBundle.getParcelableArrayList("fullRecognitionResults");

            if (localArrayList != null && localArrayList.size() > 0) {
                String str = localArrayList.get(0).toString();
                Log.i("xyz", "-------------->result:" + str);
            } else {
                Log.i("xyz", "-------------->result:null");
            }

        }

        public void onRmsChanged(float paramFloat) {
            Log.i("xyz", "---------->onRmsChanged:" + paramFloat);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mController != null) {
            mController.onStop();
            mController.onDestroy();
        }
    }
}
