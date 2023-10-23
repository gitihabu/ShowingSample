public class CardSwipeViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView titleTextView;
    TextView descriptionTextView;

    public CardSwipeViewHolder(@NonNull View itemView) {
        super(itemView);

        SwipeNewsCardBinding binding = SwipeNewsCardBinding.bind(itemView);
        imageView = binding.swipeCardImageView;
        titleTextView = binding.swipeCardTitle;
        descriptionTextView = binding.swipeCardDescription;
    }
    @NonNull
    @Override
    public CardSwipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.swipe_news_card, parent, false);
        return new CardSwipeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardSwipeViewHolder holder, int position) {
        Article article = articles.get(position);
        holder.titleTextView.setText(article.title);
        holder.descriptionTextView.setText(article.description);
        if (article.urlToImage != null) {
            Picasso.get().load(article.urlToImage).into(holder.imageView);
        }
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

}
