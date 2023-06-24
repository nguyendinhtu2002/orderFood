package com.example.orderfood;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import com.example.orderfood.Adapter.FoodAdapter;
import com.example.orderfood.Adapter.MenuAdapter;
import com.example.orderfood.Common.Common;
import com.example.orderfood.Database.MyDataBase;
import com.example.orderfood.Interface.ItemClickListener;
import com.example.orderfood.Model.Category;
import com.example.orderfood.Model.Food;
import com.example.orderfood.ViewHolder.MenuViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.orderfood.databinding.ActivityHomeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,ItemClickListener {

    private RecyclerView recycler_menu;
    private AppBarConfiguration mAppBarConfiguration;

    private RecyclerView.LayoutManager layoutManager;
    private MenuAdapter adapter;
    private MyDataBase myDatabase;
    private List<Category> menuList;
    private TextView txtFullName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Menu");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener((view) -> {
            Intent cartIntent = new Intent(Home.this, Cart.class);
            startActivity(cartIntent);
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        txtFullName = headerView.findViewById(R.id.txtFullName);
        txtFullName.setText(Common.currentUser.getName());

        recycler_menu = findViewById(R.id.recycler_menu);
        recycler_menu.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recycler_menu.setLayoutManager(layoutManager);

        myDatabase = new MyDataBase(this);

        loadMenu();
    }


    private void loadMenu() {
        menuList = myDatabase.getAllCategories();

        // Create a new instance of MenuAdapter
        MenuAdapter adapter = new MenuAdapter(menuList);

        // Set the ItemClickListener
        adapter.setItemClickListener(this);

        // Set the adapter for the RecyclerView
        recycler_menu.setAdapter(adapter);
    }

//    private void loadMenu() {
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference categoryRef = database.getReference("Category");
//
//        // Lắng nghe sự thay đổi dữ liệu trên Firebase
//        categoryRef.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                menuList = new ArrayList<>();
//                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
//                    Category category = categorySnapshot.getValue(Category.class);
//                    menuList.add(category);
//
//                    // Insert danh sách menu vào SQLite
//                    myDatabase.addCategory(category);
//                }
//
//                // Hiển thị danh sách menu trong RecyclerView
//                adapter = new MenuAdapter(menuList);
//                recycler_menu.setAdapter(adapter);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Xử lý khi có lỗi xảy ra trong quá trình đọc dữ liệu từ Firebase
//                Toast.makeText(Home.this, "Failed to load menu: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
    @Override
    public void onBackPressed () {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        return super.onOptionsItemSelected(item);
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.recycler_menu);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_cart) {
            Intent cartIntent = new Intent(Home.this, Cart.class);
            startActivity(cartIntent);
        }
        else if (id == R.id.nav_orders) {
            Intent orderIntent = new Intent(Home.this, OrderStatus.class);
            startActivity(orderIntent);
        }
        else if (id == R.id.nav_logout) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Xác nhận đăng xuất");
            builder.setMessage("Bạn có chắc chắn muốn đăng xuất?");
            builder.setPositiveButton("Đăng xuất", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    // Perform logout
                    Intent logOutIntent = new Intent(Home.this, MainActivity.class);
                    logOutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(logOutIntent);
                }
            });
            builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    @Override
    public void onclick(View v, int position, boolean isLongClick) {
        // Get the clicked category

        Category clickedCategory = menuList.get(position);
        // Pass the category to the food detail activity
        Intent intent = new Intent(Home.this, FoodList.class);
        intent.putExtra("CategoryId", String.valueOf(clickedCategory.getCategoryId()));
        startActivity(intent);
    }
}