package algonquin.cst2335.qin00038;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class MessageListFragment extends Fragment {

    RecyclerView chatList;
    ArrayList<ChatMessage> messages = new ArrayList<>();
    MyChatAdapter theAdapter;
    SQLiteDatabase db;
    Button send;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View chatLayout = inflater.inflate(R.layout.chatlayout, container, false);
        send = chatLayout.findViewById(R.id.send);

        chatList = chatLayout.findViewById(R.id.myrecycler);
        theAdapter = new MyChatAdapter();
        chatList.setAdapter(theAdapter);
        chatList.setLayoutManager(new LinearLayoutManager(getContext()));

        Button sendBtn = chatLayout.findViewById(R.id.send);
        Button receiveBtn = chatLayout.findViewById(R.id.receive);
        EditText edit = chatLayout.findViewById(R.id.editText);

        MyOpenHelper opener = new MyOpenHelper( getContext() );
        db = opener.getWritableDatabase();

        Cursor results = db.rawQuery("Select * from " + MyOpenHelper.TABLE_NAME + ";", null);

        int _idCol = results.getColumnIndex("_id");
        int messageCol = results.getColumnIndex( MyOpenHelper.col_message);
        int sendCol = results.getColumnIndex( MyOpenHelper.col_send_receive);
        int timeCol = results.getColumnIndex( MyOpenHelper.col_time_sent);
        //results.moveToNext();
        while(results.moveToNext()) {

            long id = results.getInt(_idCol);
            String message = results.getString(messageCol);
            String time = results.getString(timeCol);
            int sendOrReceive = results.getInt(sendCol);

            messages.add(new ChatMessage(message, sendOrReceive, time, id));
        }

        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());

        sendBtn.setOnClickListener(click -> {
            String whatIsTyped = edit.getText().toString();
            ChatMessage cm = new ChatMessage(whatIsTyped, 1, currentDateandTime);

            ContentValues newRow = new ContentValues();
            newRow.put(MyOpenHelper.col_message, cm.getMessage());
            newRow.put(MyOpenHelper.col_send_receive, cm.getSendOrReceive());
            newRow.put(MyOpenHelper.col_time_sent, cm.getTimeSent());
            long newId = db.insert(MyOpenHelper.TABLE_NAME, MyOpenHelper.col_message, newRow);
            cm.setId(newId);

            messages.add(cm);
            edit.setText("");
            theAdapter.notifyItemInserted(messages.size() - 1);
        });

        receiveBtn.setOnClickListener(click -> {
            String whatIsTyped = edit.getText().toString();
            ChatMessage cm = new ChatMessage(whatIsTyped, 2, currentDateandTime);

            ContentValues newRow = new ContentValues();
            newRow.put(MyOpenHelper.col_message, cm.getMessage());
            newRow.put(MyOpenHelper.col_send_receive, cm.getSendOrReceive());
            newRow.put(MyOpenHelper.col_time_sent, cm.getTimeSent());
            long newId = db.insert(MyOpenHelper.TABLE_NAME, MyOpenHelper.col_message, newRow);
            cm.setId(newId);

            messages.add(cm);
            edit.setText("");
            theAdapter.notifyItemInserted(messages.size() - 1);
        });

        return chatLayout;

    }

    public void notifyMessageDeleted(ChatMessage chosenMessage, int chosenPosition) {

                AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );
                builder.setMessage("Do you want to delete the message: " + chosenMessage.getMessage())
                        .setTitle("Question: ")
                        .setNegativeButton("No", (dislog, cl) -> {})
                        .setPositiveButton("Yes", (dislog, cl) -> {

                            ChatMessage removedMessage = messages.get(chosenPosition);
                            messages.remove(chosenPosition);
                            theAdapter.notifyItemRemoved(chosenPosition);

                            db.delete(MyOpenHelper.TABLE_NAME, "_id=?", new String[]{Long.toString(removedMessage.getId())});

                            Snackbar.make(send, "You deleted message #" + chosenPosition, Snackbar.LENGTH_LONG)
                                    .setAction("Undo", clk -> {
                                        messages.add(chosenPosition, removedMessage);
                                        theAdapter.notifyItemRemoved(chosenPosition);

                                        db.execSQL(String.format( "Insert into %s values( \"%d\", \"%s\", \"%d\", \"%s\" );",
                                                MyOpenHelper.TABLE_NAME,removedMessage.getId(), removedMessage.getMessage(),
                                                removedMessage.getSendOrReceive(), removedMessage.getTimeSent()));

                                    }).show();

                        })
                        .create().show();
    }

    private class MyRowViews extends RecyclerView.ViewHolder{

        TextView messageText;
        TextView timeText;

        public MyRowViews( View itemView) {
            super(itemView);

            itemView.setOnClickListener( click -> {

                ChatRoom parentActivity = (ChatRoom)getContext();
                int position = getAbsoluteAdapterPosition();
                parentActivity.userClickedMessage(messages.get(position), position);

            });

            messageText = itemView.findViewById(R.id.message);
            timeText = itemView.findViewById(R.id.time);

        }
    }


    private class MyChatAdapter extends RecyclerView.Adapter<MyRowViews> {

        @Override
        public MyRowViews onCreateViewHolder(ViewGroup parent, int viewType) {

            LayoutInflater inflater = getLayoutInflater();
            int layOutID;
            if (viewType == 1) {
                layOutID = R.layout.sent_message;
            } else
                layOutID = R.layout.receive_layout;

            View loadedRow = inflater.inflate(layOutID, parent, false);
            MyRowViews initRow = new MyRowViews(loadedRow);

            return initRow;
        }

        @Override
        public void onBindViewHolder(MyRowViews holder, int position) {

            holder.messageText.setText(messages.get(position).getMessage());
            holder.timeText.setText(messages.get(position).getTimeSent());

        }

        @Override
        public int getItemCount() {
            return messages.size();
        }

        public int getItemViewType(int position) {
            return messages.get(position).getSendOrReceive();
        }

    }
    class ChatMessage{

        String message;
        int sendOrReceive;
        String timeSent;
        long id;

        public ChatMessage(String message, int sendOrReceive, String timeSent) {
            this.message = message;
            this.sendOrReceive = sendOrReceive;
            this.timeSent = timeSent;
        }

        public ChatMessage(String message, int sendOrReceive, String timeSent, long id) {
            this.message = message;
            this.sendOrReceive = sendOrReceive;
            this.timeSent = timeSent;
            setId(id);
        }
        public void setId( long l) { id = l; };

        public long getId() {
            return id;
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