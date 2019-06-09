package com.example.thequiz;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class VraagAdapter extends RecyclerView.Adapter<VraagAdapter.VraagViewHolder> {
    private Context mContext;
    private Cursor mCursor;

    public VraagAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public class VraagViewHolder extends RecyclerView.ViewHolder {
        public TextView vraagText;


        public VraagViewHolder(View itemView) {
            super(itemView);

            vraagText = itemView.findViewById(R.id.textview_vraag_item);

        }
    }

    @Override
    public VraagViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.vraag_item, parent, false);
        return new VraagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VraagViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        String vraag = mCursor.getString(mCursor.getColumnIndex(QuizContract.QuestionsTable.COLUMN_QUESTION));
        long id = mCursor.getLong(mCursor.getColumnIndex(QuizContract.QuestionsTable._ID));


        holder.vraagText.setText(vraag);
        holder.itemView.setTag(id);

    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }

        mCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }
}
