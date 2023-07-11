package com.apex.codeassesment.ui.main

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.apex.codeassesment.data.UserRepository
import com.apex.codeassesment.data.model.HomeUiState
import com.apex.codeassesment.data.model.User
import com.apex.codeassesment.databinding.ActivityMainBinding
import com.apex.codeassesment.di.MainComponent
import com.apex.codeassesment.ui.MainViewModel
import com.apex.codeassesment.ui.adapter.UserRVAdapter
import com.apex.codeassesment.ui.details.DetailsActivity
import com.bumptech.glide.Glide
import javax.inject.Inject

// TODO (5 points): Move calls to repository to Presenter or ViewModel.
// TODO (5 points): Use combination of sealed/Dataclasses for exposing the data required by the view from viewModel .
// TODO (3 points): Add tests for viewModel or presenter.
// TODO (1 point): Add content description to images
// TODO (3 points): Add tests
// TODO (Optional Bonus 10 points): Make a copy of this activity with different name and convert the current layout it is using in
//  Jetpack Compose.
@SuppressLint("NotifyDataSetChanged")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    // TODO (2 points): Convert to view binding
//  private var userImageView: ImageView? = null
//  private var userNameTextView: TextView? = null
//  private var userEmailTextView: TextView? = null
//  private var seeDetailsButton: Button? = null
//  private var refreshUserButton: Button? = null
//  private var showUserListButton: Button? = null
//  private var userListView: ListView? = null

    @Inject
    lateinit var userRepository: UserRepository
    @Inject
    lateinit var mainViewModelFactory: MainViewModel.Factory
    private val viewModel: MainViewModel by viewModels { mainViewModelFactory }
    private val userRVAdapter = UserRVAdapter()
    private var userList: List<User> = arrayListOf()
        set(value) {
            field = value
            userRVAdapter.users = value
            userRVAdapter.notifyDataSetChanged()
        }

    private var randomUser: User = User()
        set(value) {
            Log.d("API-Result", ": $value")
            Glide.with(this).load(value.picture?.large).into(binding.mainImage)
            // TODO (1 point): Use Glide to load images after getting the data from endpoints mentioned in RemoteDataSource
            // userImageView.setImageBitmap(refreshedUser.image)
            value.name?.first?.let {
                binding.mainName.text = it
            }
            binding.mainEmail.text = value.email
            field = value
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        (applicationContext as MainComponent.Injector).mainComponent.inject(this)


//    userImageView = findViewById(R.id.main_image)
//    userNameTextView = findViewById(R.id.main_name)
//    userEmailTextView = findViewById(R.id.main_email)
//    seeDetailsButton = findViewById(R.id.main_see_details_button)
//    refreshUserButton = findViewById(R.id.main_refresh_button)
//    showUserListButton = findViewById(R.id.main_user_list_button)
//    userListView = findViewById(R.id.main_user_list)

        userRVAdapter.users = userList
        binding.mainUserList.adapter = userRVAdapter
        userRVAdapter.clickListener = {
            it.navigateDetails(this)
        }

        randomUser = userRepository.getSavedUser()

        binding.mainSeeDetailsButton.setOnClickListener { randomUser.navigateDetails(this) }

        binding.mainRefreshButton.setOnClickListener {
            viewModel.getUser()
        }

        binding.mainUserListButton.setOnClickListener {
            viewModel.getUsers()

        }
        setupObservers()
    }

    private fun setupObservers() {
        viewModel.userLiveData.observe(this) {state->
            when (state) {
                is HomeUiState.Success -> {
                    userList = state.users
                }
                is HomeUiState.Error -> {
                    Toast.makeText(this@MainActivity, state.message, Toast.LENGTH_SHORT).show()
                }
                is HomeUiState.SingleUserSuccess -> {
                    randomUser = state.user
                }
                is HomeUiState.Loading -> {
                    Toast.makeText(this@MainActivity, "Loading..", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    // TODO (2 points): Convert to extension function.
    private fun User.navigateDetails(context: AppCompatActivity) {
        val putExtra = Intent(context, DetailsActivity::class.java).putExtra("saved-user-key", this)
        startActivity(putExtra)
    }
}
