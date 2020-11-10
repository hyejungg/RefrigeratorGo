package com.example.refrigeratorgo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.Arrays;
import java.util.List;

public class UserAccoutFragment1 extends Fragment {

    private UserRecyclerAdapter adapter;
    View view;

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.useraccount_fragment1, null);

        RecyclerView recyclerView = view.findViewById(R.id.rvF1);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        adapter = new UserRecyclerAdapter();
        recyclerView.setAdapter(adapter);

        getData();

        return view;
    }

    private void getData() {
        // 임의의 데이터
        List<String> listUser = Arrays.asList("User0", "User1", "User2", "User3", "User4", "User5", "User6", "User7",
                "User8", "User9", "User10", "User11", "User12", "User13", "User14", "User15");
        List<String> listEmail = Arrays.asList(
                "user0@email.com",
                "user1@email.com",
                "user2@email.com",
                "user3@email.com",
                "user4@email.com",
                "user5@email.com",
                "user6@email.com",
                "user7@email.com",
                "user8@email.com",
                "user9@email.com",
                "user10@email.com",
                "user11@email.com",
                "user12@email.com",
                "user13@email.com",
                "user14@email.com",
                "user15@email.com"
        );
        List<String> listBtn = Arrays.asList(
                "연결해제",
                "연결해제",
                "연결해제",
                "연결해제",
                "연결해제",
                "연결해제",
                "연결해제",
                "연결해제",
                "연결해제",
                "연결해제",
                "연결해제",
                "연결해제",
                "연결해제",
                "연결해제",
                "연결해제",
                "연결해제"
        );
        List<Integer> listResId = Arrays.asList(
                R.drawable.user_image1,
                R.drawable.user_image1,
                R.drawable.user_image1,
                R.drawable.user_image1,
                R.drawable.user_image1,
                R.drawable.user_image1,
                R.drawable.user_image1,
                R.drawable.user_image1,
                R.drawable.user_image1,
                R.drawable.user_image1,
                R.drawable.user_image1,
                R.drawable.user_image1,
                R.drawable.user_image1,
                R.drawable.user_image1,
                R.drawable.user_image1,
                R.drawable.user_image1
        );
        for (int i = 0; i < listUser.size(); i++) {
            // 각 List의 값들을 data 객체에 set 해줌
            User data = new User();
            data.setTitle(listUser.get(i));
            data.setContent(listEmail.get(i));
            data.setBtn(listBtn.get(i));
            data.setResId(listResId.get(i));

            // 각 값이 들어간 data를 adapter에 추가
            adapter.addItem(data);
        }
    }
}