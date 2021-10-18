package algonquin.cst2335.qin00038;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class ChatRoom extends AppCompatActivity {

    RecyclerView chatList;
    ArrayList<ChatMessage> messages = new ArrayList<>();
    MyChatAdapter theAdapter = new MyChatAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.chatlayout);
        chatList = findViewById(R.id.myrecycler);

        chatList.setAdapter(theAdapter);
        chatList.setLayoutManager(new LinearLayoutManager(this));

        Button sendBtn = findViewById(R.id.send);;
        Button receiveBtn = findViewById(R.id.receive);
        EditText edit = findViewById(R.id.editText);

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        ChatMessage thisMessage = new ChatMessage( "123", 1,currentDateandTime );

        sendBtn.setOnClickListener( click ->{
            String whatIsTyped = edit.getText().toString();
            messages.add(new ChatMessage(whatIsTyped,1,currentDateandTime));
            edit.setText("");
            theAdapter.notifyItemInserted( messages.size()-1 );
        } );

        receiveBtn.setOnClickListener( click ->{
            String whatIsTyped = edit.getText().toString();
            messages.add(new ChatMessage(whatIsTyped,2,currentDateandTime));
            edit.setText("");
            theAdapter.notifyItemInserted( messages.size()-1 );
        } );


    }

    private class MyRowViews extends RecyclerView.ViewHolder{

        TextView messageText;
        TextView timeText;

        public MyRowViews( View itemView) {
            super(itemView);

            itemView.setOnClickListener( click -> {

                int position = getAbsoluteAdapterPosition();
                AlertDialog.Builder builder = new AlertDialog.Builder( ChatRoom.this );
                builder.setMessage("Do you want to delete the message: " + messageText.getText())
                .setTitle("Question: ")
                .setNegativeButton("No", (dislog, cl) -> {})
                .setPositiveButton("Yes", (dislog, cl) -> {

                    ChatMessage removedMessage = messages.get(position);
                    messages.remove(position);
                    theAdapter.notifyItemRemoved(position);

                    Snackbar.make(messageText, "You deleted message #" + position, Snackbar.LENGTH_LONG)
                            .setAction("Undo", clk -> {
                                messages.add(position, removedMessage);
                                theAdapter.notifyItemRemoved(position);

                            }).show();



                })
                        .create().show();

            });



            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);

        }
    }

    private class MyChatAdapter extends RecyclerView.Adapter<MyRowViews>{

      @Override
      public MyRowViews  onCreateViewHolder(ViewGroup parent, int viewType) {

          LayoutInflater inflater = getLayoutInflater();
          int layOutID;
          if(viewType==1){
              layOutID= R.layout.sent_message;
          }else
              layOutID= R.layout.receive_layout;

          View loadedRow = inflater.inflate(layOutID, parent, false);
          MyRowViews initRow = new MyRowViews(loadedRow);

        return initRow;
      }

      @Override
      public void onBindViewHolder(MyRowViews  holder, int position) {

          holder.messageText.setText(messages.get(position).getMessage());
          holder.timeText.setText(messages.get(position).getTimeSent());

      }

      @Override
      public int getItemCount() {
        return messages.size();
      }

        public int getItemViewType(int position){
            return messages.get(position).getSendOrReceive();
        }

    }
    private class ChatMessage{

        String message;
        int sendOrReceive;
        String timeSent;

        public ChatMessage(String message, int sendOrReceive, String timeSent) {
            this.message = message;
            this.sendOrReceive = sendOrReceive;
            this.timeSent = timeSent;
        }

        public String getMessage() {
            return message;
        }

        public int getSendOrReceive() {
            return sendOrReceive;
        }

        public String getTimeSent() {
            return timeSent;
        }
    }




}

