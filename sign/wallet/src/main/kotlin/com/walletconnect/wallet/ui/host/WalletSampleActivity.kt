package com.walletconnect.wallet.ui.host

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.walletconnect.sample_common.tag
import com.walletconnect.sample_common.viewBinding
import com.walletconnect.wallet.R
import com.walletconnect.wallet.SESSION_REQUEST_ARGS_NUM_KEY
import com.walletconnect.wallet.SESSION_REQUEST_KEY
import com.walletconnect.wallet.databinding.ActivityWalletBinding
import com.walletconnect.wallet.ui.SampleWalletEvents
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class WalletSampleActivity : AppCompatActivity() {
    private val binding by viewBinding(ActivityWalletBinding::inflate)
    private val viewModel: WalletSampleViewModel by viewModels()
    private val navController by lazy {
        (supportFragmentManager.findFragmentById(R.id.fcvHost) as NavHostFragment).navController
    }
    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // TODO: Inform user that that your app will not show notifications.
        }
    }

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // TODO: display an educational UI explaining to the user the features that will be enabled
                //       by them granting the POST_NOTIFICATION permission. This UI should provide the user
                //       "OK" and "No thanks" buttons. If the user selects "OK," directly request the permission.
                //       If the user selects "No thanks," allow the user to continue without notifications.
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(binding.root)

        askNotificationPermission()

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(tag(this::class), "Fetching FCM registration token failed", task.exception)
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            // Log and toast
            val msg = "Testing Token: $token"
            Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
        })

        viewModel.events
            .flowWithLifecycle(lifecycle)
            .onEach { event ->
                when (event) {
                    is SampleWalletEvents.SessionProposal -> navController.navigate(R.id.action_global_to_session_proposal)
                    is SampleWalletEvents.SessionRequest -> {
                        navController.navigate(R.id.action_global_to_session_request,
                            bundleOf(SESSION_REQUEST_KEY to event.arrayOfArgs, SESSION_REQUEST_ARGS_NUM_KEY to event.numOfArgs))
                    }
                    else -> Unit
                }
            }
            .launchIn(lifecycleScope)

        setupActionBarWithNavController(navController, AppBarConfiguration(setOf(R.id.fragment_accounts, R.id.fragment_active_sessions)))
        binding.bnvTabs.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bnvTabs.isVisible = destination.id != R.id.fragment_scanner
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        navController.handleDeepLink(intent)
    }
}