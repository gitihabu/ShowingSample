package ui.home;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.laioffer.tinnews.R;
import com.laioffer.tinnews.repository.NewsRepository;

import java.util.Comparator;
import java.util.PriorityQueue;

public class HomeFragment extends Fragment {
    private HomeViewModel viewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NewsRepository repository = new NewsRepository();
        viewModel = new HomeViewModel(repository);
        viewModel.setCountryInput("us");
        viewModel
                .getTopHeadlines()
                .observe(
                        getViewLifecycleOwner(),
                        newsResponse -> {
                            if (newsResponse != null) {
                                Log.d("ui.home.HomeFragment.HomeFragment", newsResponse.toString());
                            }
                        });

    }

    public static class HomeFragment extends Fragment implements CardStackListener{
        private HomeViewModel viewModel;
        private FragmentHomeBinding binding;
        private CardStackLayoutManager layoutManager;
        private CardSwipeAdapter swipeAdapter;


        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            // Inflate the layout for this fragment
    //        return inflater.inflate(R.layout.fragment_home, container, false);
            binding =  FragmentHomeBinding.inflate(inflater, container, false);
            return binding.getRoot();
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            swipeAdapter = new CardSwipeAdapter();
            layoutManager = new CardStackLayoutManager(getContext(), this);
            layoutManager.setStackFrom(StackFrom.Top);
            binding.homeCardStackView.setLayoutManager(layoutManager);
            binding.homeCardStackView.setAdapter(swipeAdapter);

            binding.homeLikeButton.setOnClickListener(v -> {
                swipeCard(Direction.Right);
            });
            binding.homeLikeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    swipeCard(Direction.Right);
                }
            });

            PriorityQueue<Integer> priorityQueue = new PriorityQueue<>(new Comparator<Integer>() {
                @Override
                public int compare(Integer o1, Integer o2) {
                    return 0;
                }
            });
            binding.homeUnlikeButton.setOnClickListener(v -> {
                swipeCard(Direction.Left);
            });

            NewsRepository repository = new NewsRepository();
            viewModel = new ViewModelProvider(this, new NewsViewModelFactory(repository)).get(HomeViewModel.class);
            viewModel
                    .getTopHeadlines()
                    .observe(
                            getViewLifecycleOwner(),
                            newsResponse -> {
                                if (newsResponse != null) {
                                    swipeAdapter.setArticles(newsResponse.articles);
                                    Log.d("ui.home.HomeFragment.HomeFragment", newsResponse.toString());
                                }
                            });
            viewModel.setCountryInput("us");
        }

        private void swipeCard(Direction direction) {
            SwipeAnimationSetting setting = new SwipeAnimationSetting.Builder()
                    .setDirection(direction)
                    .setDuration(Duration.Normal.duration)
                    .build();
            layoutManager.setSwipeAnimationSetting(setting);
            binding.homeCardStackView.swipe();
        }

        @Override
        public void onCardDragging(Direction direction, float v) {

        }

        @Override
        public void onCardSwiped(Direction direction) {
            if (direction == Direction.Left) {
                Log.d("CardStackView", "Unliked " + layoutManager.getTopPosition());
            } else if (direction == Direction.Right) {
                Log.d("CardStackView", "Like " + layoutManager.getTopPosition());

            }
        }

        @Override
        public void onCardRewound() {

        }

        @Override
        public void onCardCanceled() {

        }

        @Override
        public void onCardAppeared(View view, int i) {

        }

        @Override
        public void onCardDisappeared(View view, int i) {

        }


    }
}

