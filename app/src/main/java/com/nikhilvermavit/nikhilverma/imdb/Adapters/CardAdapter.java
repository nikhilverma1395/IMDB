        package com.nikhilvermavit.nikhilverma.imdb.Adapters;

        import android.content.Context;
        import android.graphics.Typeface;
        import android.support.v7.widget.RecyclerView;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ImageView;
        import android.widget.LinearLayout;
        import android.widget.TextView;

        import com.nikhilvermavit.nikhilverma.imdb.Models.ActorDetailModel;
        import com.nikhilvermavit.nikhilverma.imdb.R;
        import com.gc.materialdesign.views.ProgressBarCircularIndeterminate;
        import com.squareup.picasso.Callback;
        import com.squareup.picasso.Picasso;

        import java.util.List;

        /**
         * Created by Nikhil Verma on 04-01-2015.
         */
        public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
           public static boolean ImageLoaded;
            private static List<ActorDetailModel> cardList;
            private Context context = null;
            private int Colorspriv[] = {0xE690A4AE, 0xE6FF6E40, 0xE6BDBDBD, 0xE6FFCCBC, 0xE6BCAAA4, 0xE6FFAB40,
                    0xE6FFE57F, 0xE6FFA000, 0xE6FFEB3B, 0xE6FFB74D, 0xE669F0AE, 0xE6CCFF90, 0xE6EEFF41, 0xE69CCC65, 0xE6E6EE9C, 0xE6004D40, 0xE60277BD, 0xE600ACC1, 0xE6009688
                    , 0xE62962FF, 0xE63F51B5, 0xE6F44336, 0xE6BA68C8, 0xE6D81B60};

            public CardAdapter(List<ActorDetailModel> list, Context context) {
                ImageLoaded = false;
                this.cardList = list;
                this.context = context;
            }

            public static void nullit() {
                cardList = null;
            }


            @Override
            public CardAdapter.CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.actor_r, viewGroup, false);
                return (new CardViewHolder(itemView));

            }

            @Override
            public void onBindViewHolder(final CardAdapter.CardViewHolder cardViewHolder, int i) {
                final ActorDetailModel cl = cardList.get(i);
                cardViewHolder.name.setText("\t" + cl.getActorName());
                Typeface tr = Typeface.createFromAsset(context.getAssets(), "sharptxt.TTF");
                cardViewHolder.name.setTypeface(tr);
                String act = cl.getActorName();
                int charVA = (int) act.charAt(act.length() / 2 + 1);
                int colorv = Colorspriv[charVA % 24];
                cardViewHolder.name.setBackgroundColor(colorv);
                cardViewHolder.ll_shaded_urls.setBackgroundColor(colorv);
                cardViewHolder.ll_shaded_urls.setAlpha(0.2f);
                cardViewHolder.character.setText("Character Name :\t" + cl.getCharacter());
                if (cl.getUrlCharacter() == "http://www.imdb.com")
                    cardViewHolder.urlCharacter.setText("Character Profile :\t" + "\t\t N/A \t\t");
                else
                    cardViewHolder.urlCharacter.setText("Character Profile :\t" + cl.getUrlCharacter());

                if (cl.getUrlProfile() == "http://www.imdb.com")
                    cardViewHolder.urlProfile.setText(" Actor Profile :\t" + "\t\t N/A \t\t");
                else
                    cardViewHolder.urlProfile.setText(" Actor Profile  :\t" + cl.getUrlProfile());
                cardViewHolder.character.setTypeface(tr);
                cardViewHolder.urlCharacter.setTypeface(tr);
                cardViewHolder.urlProfile.setTypeface(tr);
                Picasso.with(context)
                        .load(cl.getUrlPhoto())
                        .resize(200, 300)
                        .error(R.drawable.images)
                        .into(cardViewHolder.urlPhoto, new Callback() {
                            @Override
                            public void onSuccess() {
                                cardViewHolder.pb_b.setVisibility(View.INVISIBLE);
                            }

                            @Override
                            public void onError() {
                                cardViewHolder.pb_b.setVisibility(View.GONE);
                            }
                        });
                //  cardViewHolder.fab
            }

            @Override
            public int getItemCount() {
                return cardList.size();
            }


            public class CardViewHolder extends RecyclerView.ViewHolder{
                protected ImageView urlPhoto;
                protected TextView name, character, urlCharacter, urlProfile;
                ProgressBarCircularIndeterminate pb_b;
                LinearLayout ll_shaded_urls;

                public CardViewHolder(View v) {
                    super(v);
                    ll_shaded_urls = (LinearLayout) v.findViewById(R.id.ll_shaded_urls);
                    name = (TextView) v.findViewById(R.id.urlactorname);
                    character = (TextView) v.findViewById(R.id.character);
                    urlCharacter = (TextView) v.findViewById(R.id.urlcharacter);
                    urlProfile = (TextView) v.findViewById(R.id.urlprofile);
                    urlPhoto = (ImageView) v.findViewById(R.id.urlPhoto);
                    pb_b = (ProgressBarCircularIndeterminate) v.findViewById(R.id.progressBar_card);
                }



            }
        }
