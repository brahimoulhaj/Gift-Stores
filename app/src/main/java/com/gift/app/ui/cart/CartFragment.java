package com.gift.app.ui.cart;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gift.app.R;
import com.gift.app.data.models.CartModel;
import com.gift.app.databinding.CartFragmentBinding;
import com.gift.app.ui.Home.products.ProductsViewModel;
import com.gift.app.ui.Home.stores.AdapterStores;
import com.gift.app.utils.Extensions;

public class CartFragment extends Fragment implements AdapterCart.CartCallback, SwipeRefreshLayout.OnRefreshListener {

    private CartViewModel mViewModel;
    private ProductsViewModel mViewModelProducts;

    CartFragmentBinding binding;

    private AdapterCart adapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.cart_fragment, container, false);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CartViewModel.class);
        mViewModelProducts = new ViewModelProvider(this).get(ProductsViewModel.class);

        mViewModel.getCart();
        setUiState();
        onCartActionResponse();
        onConfirmOrderResponse();
        confirmOrder();
        binding.cartSwipe.setOnRefreshListener(this);

        binding.backImgV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back();
            }
        });
    }

    private void back() {
        NavHostFragment.findNavController(this).navigateUp();
    }


    private void setUiState() {
        mViewModel.liveState.observe(getViewLifecycleOwner(), state -> {

            if (state.onLoading) {
                binding.loading.setVisibility(View.VISIBLE);
                binding.cartRv.setVisibility(View.GONE);
            }
            if (state.onSuccess) {
                binding.loading.setVisibility(View.GONE);
                binding.cartRv.setVisibility(View.VISIBLE);
                onSuccess();
            }
            if (state.onError) {
                binding.loading.setVisibility(View.GONE);
                binding.cartRv.setVisibility(View.GONE);
                Extensions.generalErrorSnakeBar(binding.storesRoot);
            }
            if (state.onEmpty) {
                binding.cartRv.setVisibility(View.GONE);
                binding.loading.setVisibility(View.GONE);
                binding.emptyViewLayout.setVisibility(View.VISIBLE);
                binding.orderPriceLayout.setVisibility(View.GONE);
                binding.confirmTv.setVisibility(View.GONE);

            }
            if (state.onNoConnection) {
                binding.loading.setVisibility(View.GONE);
                binding.cartRv.setVisibility(View.GONE);
                Extensions.noInternetSnakeBar(binding.storesRoot);

            }

        });
    }

    private void onSuccess() {
        binding.totalPriceTv.setText(mViewModel.response.getAll());
        binding.confirmTv.setVisibility(View.VISIBLE);
        binding.orderPriceLayout.setVisibility(View.VISIBLE);
        adapter = new AdapterCart(mViewModel.response.getData(), requireActivity(), this);
        binding.cartRv.setAdapter(adapter);
        adapter.notifyDataSetChanged();

    }

    private void confirmOrder() {
        binding.confirmTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mViewModel.addOrder();
            }
        });
    }

    private void onConfirmOrderResponse() {
        mViewModel.liveStateOrder.observe(getViewLifecycleOwner(), state -> {
            if (state.onLoading) {
                binding.loading.setVisibility(View.VISIBLE);
            }
            if (state.onSuccess) {
                binding.loading.setVisibility(View.GONE);
                if (!mViewModel.response.getMsg().isEmpty())
                    Extensions.Success(binding.storesRoot, mViewModel.responseOrder.getMsg());
                mViewModel.getCart();
                mViewModel.response.setMsg("");

            }
            if (state.onError) {
                binding.loading.setVisibility(View.GONE);
                Extensions.Success(binding.storesRoot, mViewModel.responseOrder.getMsg());
            }
            if (state.onNoConnection) {
                binding.loading.setVisibility(View.GONE);
                Extensions.noInternetSnakeBar(binding.storesRoot);

            }

        });
    }


    private void onCartActionResponse() {
        mViewModelProducts.liveStateCart.observe(getViewLifecycleOwner(), state -> {
            if (state.onLoading) {
                binding.loading.setVisibility(View.VISIBLE);
            }
            if (state.onSuccess) {
                binding.loading.setVisibility(View.GONE);
                if (!mViewModelProducts.response.getMsg().isEmpty())
                    Extensions.Success(binding.storesRoot, mViewModelProducts.response.getMsg());
                mViewModel.getCart();
                mViewModelProducts.response.setMsg("");

            }
            if (state.onError) {
                binding.loading.setVisibility(View.GONE);
                Extensions.Success(binding.storesRoot, mViewModelProducts.response.getMsg());
            }
            if (state.onNoConnection) {
                binding.loading.setVisibility(View.GONE);
                Extensions.noInternetSnakeBar(binding.storesRoot);

            }

        });
    }

    @Override
    public void addCartClicked(CartModel model) {
        mViewModelProducts.productId = model.getId().toString();
        mViewModelProducts.addCart();
    }

    @Override
    public void removeCartClicked(CartModel model) {
        mViewModelProducts.productId = model.getId().toString();
        mViewModelProducts.delCart();
    }

    @Override
    public void onRefresh() {
        binding.cartSwipe.setRefreshing(false);
        mViewModel.getCart();
    }
}