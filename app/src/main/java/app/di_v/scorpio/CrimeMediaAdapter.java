package app.di_v.scorpio;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import app.di_v.scorpio.crime.CrimeMedia;


public class CrimeMediaAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {
    private List<CrimeMedia> mCrimeMedia;

    public void setCrimes(List<CrimeMedia> crimes) {
        mCrimeMedia = crimes;
    }

    public CrimeMediaAdapter(List<CrimeMedia>  crimes) {
        mCrimeMedia = crimes;
    }

    @Override
    public int getItemCount() {
        return mCrimeMedia.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_crime_photo, parent, false);
        return new CrimeMediaAdapter.CrimeHolder(cv);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        final CardView cardView = ((CrimeMediaAdapter.CrimeHolder) holder).cardView;
        ImageView img = cardView.findViewById(R.id.crime_photo);
        TextView imgName = cardView.findViewById(R.id.crime_photo_name);

        imgName.setText(mCrimeMedia.get(position).getFile());

        File filesDir = cardView.getContext().getFilesDir();
        CrimeMedia photoName = mCrimeMedia.get(position);

        Bitmap bitmap = BitmapFactory.decodeFile((new File(filesDir, photoName.getFile())).getPath());
        img.setImageBitmap(bitmap);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(cardView.getContext(), CrimeActivity.class);
                //intent.putExtra(ChatActivity.E)
                //cardView.getContext().startActivity(intent);
            }
        });
    }

    /**
     * Holder - определяет представление для каждого элемена RecyclerView
     */
    private class CrimeHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public CrimeHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }
}
