package comcesar1287.github.www.ssimperium.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import comcesar1287.github.www.ssimperium.R;
import comcesar1287.github.www.ssimperium.controller.domain.Item;
import comcesar1287.github.www.ssimperium.controller.fragment.ItemFragment;
import es.dmoral.toasty.Toasty;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private ItemFragment frag;

    private ArrayList<Item> itemsList;

    private ProgressDialog dialog;

    private DatabaseReference mDatabase;

    private Query item;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        dialog = ProgressDialog.show(this,"", this.getResources().getString(R.string.loading_items_pls_wait), true, false);

        setupUI();

        loadList();
    }

    private void setupUI() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        item = mDatabase
                .child("best_sellers")
                .orderByChild("price");
    }

    public List<Item> getItemsList() {
        return itemsList;
    }

    public void loadList(){

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                itemsList = new ArrayList<>();

                Item item;
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    item = new Item();
                    item.setPhoto((String) postSnapshot.child("photo").getValue());
                    item.setName((String) postSnapshot.child("name").getValue());
                    item.setPrice((String) postSnapshot.child("price").getValue());

                    itemsList.add(item);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dialog.dismiss();
                Toasty.error(MainActivity.this, getResources().getString(R.string.error_loading_items), Toast.LENGTH_SHORT, true).show();
                finish();
            }
        };

        ValueEventListener singleValueEventListener = new ValueEventListener() {
            public void onDataChange(DataSnapshot dataSnapshot) {

                frag = (ItemFragment) getSupportFragmentManager().findFragmentByTag("mainFrag");
                if (frag == null) {
                    frag = new ItemFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.items_fragment_container, frag, "mainFrag");
                    ft.commit();
                }

                dialog.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                dialog.dismiss();
                Toasty.error(MainActivity.this, getResources().getString(R.string.error_loading_items), Toast.LENGTH_SHORT, true).show();
                finish();
            }
        };

        item.addValueEventListener(valueEventListener);

        item.addListenerForSingleValueEvent(singleValueEventListener);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
