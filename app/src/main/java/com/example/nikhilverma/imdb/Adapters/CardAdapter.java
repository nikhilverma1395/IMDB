                    package com.example.nikhilverma.imdb.Adapters;

                    import android.content.Context;
                    import android.support.v7.widget.RecyclerView;
                    import android.view.LayoutInflater;
                    import android.view.View;
                    import android.view.ViewGroup;
                    import android.widget.ImageView;
                    import android.widget.TextView;

                    import com.example.nikhilverma.imdb.Models.ActorDetailModel;
                    import com.example.nikhilverma.imdb.R;
                    import com.example.nikhilverma.imdb.Views.RoundedTransformation;
                    import com.squareup.picasso.Picasso;

                    import java.util.List;

                    /**
                     * Created by Nikhil Verma on 04-01-2015.
                     */
                    public class CardAdapter extends RecyclerView.Adapter<CardAdapter.CardViewHolder> {
                        private static List<ActorDetailModel> cardList;
                        private Context context = null;
                        OnItemClickListener mItemClickListener;

                        public static void nullit() {
                            cardList = null;
                        }

                        public CardAdapter(List<ActorDetailModel> list, Context context) {
                            this.cardList = list;
                            this.context = context;
                        }

                        @Override
                        public CardAdapter.CardViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                            View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.actor, viewGroup, false);
                            return (new CardViewHolder(itemView));

                        }

                        @Override
                        public void onBindViewHolder(CardAdapter.CardViewHolder cardViewHolder, int i) {
                            ActorDetailModel cl = cardList.get(i);
                            cardViewHolder.name.setText("Actor Name :\t" + cl.getActorName());
                            cardViewHolder.character.setText("Character Name :\t" + cl.getCharacter());
                            if (cl.getUrlCharacter() == "http://www.imdb.com")
                                cardViewHolder.urlCharacter.setText("Character Profile :\t" + "\t\t N/A \t\t");
                            else
                                cardViewHolder.urlCharacter.setText("Character Profile :\t" + cl.getUrlCharacter());

                            if (cl.getUrlProfile() == "http://www.imdb.com")
                                cardViewHolder.urlProfile.setText(" Actor Profile :\t" + "\t\t N/A \t\t");
                            else
                                cardViewHolder.urlProfile.setText(" Actor Profile  :\t" + cl.getUrlProfile());

                            Picasso.with(context)
                                    .load(cl.getUrlPhoto())
                                    .resize(500, 500)
                                    .error(R.drawable.images)
                                    .into(cardViewHolder.urlPhoto);

                        }

                        public class CardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
                            protected ImageView urlPhoto;
                            protected TextView name, character, urlCharacter, urlProfile;


                            public CardViewHolder(View v) {
                                super(v);
                                name = (TextView) v.findViewById(R.id.urlactorname);
                                character = (TextView) v.findViewById(R.id.character);
                                urlCharacter = (TextView) v.findViewById(R.id.urlcharacter);
                                urlProfile = (TextView) v.findViewById(R.id.urlprofile);
                                urlPhoto = (ImageView) v.findViewById(R.id.urlPhoto);
                            }

                            @Override
                            public void onClick(View v) {
                                if (mItemClickListener != null) {
                                    mItemClickListener.onItemClick(v, getPosition());
                                }
                            }
                        }


                        @Override
                        public int getItemCount() {
                            return cardList.size();
                        }

                        public interface OnItemClickListener {
                            public void onItemClick(View v, int position);
                        }

                        public void SetOnItemClickListener(final OnItemClickListener mItemClickListener) {
                            this.mItemClickListener = mItemClickListener;
                        }
                    }
