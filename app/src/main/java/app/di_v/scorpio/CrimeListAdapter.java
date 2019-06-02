package app.di_v.scorpio;

import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import app.di_v.scorpio.crime.Crime;

public class CrimeListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<Crime> mCrime;

    public CrimeListAdapter(List<Crime> crimes) {
        mCrime = crimes;
    }

    public void setCrimes(List<Crime> crimes) {
        mCrime = crimes;
    }

    @Override
    public int getItemCount() {
        return mCrime.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {

        CardView cv = (CardView) LayoutInflater.from(parent.getContext())
            .inflate(R.layout.card_crime, parent, false);
        return new CrimeHolder(cv);
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

         final CardView cardView = ((CrimeHolder) holder).cardView;
         final Crime crime = mCrime.get(position);

         TextView textView = cardView.findViewById(R.id.crime_title);
         textView.setText(crime.getTitle() + " № " + crime.getNumCrime());

         TextView dateView = cardView.findViewById(R.id.crime_date);
         dateView.setText(DateFormat.format("dd.MM",
                 crime.getDate()).toString());

         cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                Intent intent = CrimeActivity.newIntent(cardView.getContext(),
                        crime.getId());
                cardView.getContext().startActivity(intent);
           }
            });
    }

    /**
     * Holder - определяет представление для каждого элемена RecyclerView
     */
    public class CrimeHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public CrimeHolder(CardView v) {
            super(v);
            cardView = v;
        }
    }
}