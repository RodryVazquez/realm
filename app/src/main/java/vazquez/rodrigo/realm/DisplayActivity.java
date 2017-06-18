package vazquez.rodrigo.realm;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import io.realm.Realm;
import io.realm.RealmResults;
import vazquez.rodrigo.realm.Models.User;


public class DisplayActivity extends AppCompatActivity {

	private Realm myRealm;
	private UsersAdapter adapter;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_display);

		myRealm = Realm.getDefaultInstance();
		setupRecyclerView();
	}

	@Override
	protected void onResume() {
		super.onResume();

		if (adapter != null)
			adapter.notifyDataSetChanged();
	}

	/**
	 * Inicializa el Recycler View
	 */
	private void setupRecyclerView() {

		//Listamos todos los registros
		RealmResults<User> userList = myRealm.where(User.class).findAll();
		//Asignamos el adapter
		RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
		adapter = new UsersAdapter(this, myRealm, userList);

		//Set layout manager
		LinearLayoutManager layoutManager = new LinearLayoutManager(this);
		layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
		recyclerView.setLayoutManager(layoutManager);

		//Asignamos el adapter al recycler view
		recyclerView.setAdapter(adapter);
	}

	/**
	 * Cerramos la instancia de realm
	 * al destruir la actividad
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		myRealm.close();
	}
}
