package com.akash.gkgsmaster.ui.splash

import android.animation.*
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.HapticFeedbackConstants
import android.view.View
import android.view.animation.*
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.akash.gkgsmaster.MainActivity
import com.akash.gkgsmaster.R
import com.akash.gkgsmaster.databinding.ActivitySplashBinding
import com.akash.gkgsmaster.ui.auth.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

@SuppressLint("CustomSplashScreen")
@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupVersionText()
        startPremiumAnimations()
        handleNavigation()
    }

    private fun setupVersionText() {
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        binding.textViewVersion.text = "V ${packageInfo.versionName}"
    }

    private fun startPremiumAnimations() {
        // 1. Initial State
        binding.imageViewLogo.alpha = 0f
        binding.imageViewLogo.scaleX = 0.5f
        binding.imageViewLogo.scaleY = 0.5f
        binding.viewGlass.alpha = 0f
        binding.viewGlow.alpha = 0f

        // 2. Logo Entrance with Haptic
        binding.imageViewLogo.animate()
            .alpha(1f)
            .scaleX(1f)
            .scaleY(1f)
            .setDuration(1000)
            .setInterpolator(OvershootInterpolator())
            .withStartAction {
                binding.root.performHapticFeedback(HapticFeedbackConstants.LONG_PRESS)
            }
            .start()

        // 3. Glass & Glow Entrance
        binding.viewGlass.animate().alpha(1f).setDuration(1200).setStartDelay(200).start()
        binding.viewGlow.animate().alpha(0.8f).setDuration(1500).setStartDelay(400).start()

        // 4. Fire Ring & Light Rays Infinite Rotation
        val rotateAnim = AnimationUtils.loadAnimation(this, R.anim.fire_rotation)
        binding.viewFireRing.startAnimation(rotateAnim)
        
        val slowRotate = RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        slowRotate.duration = 15000
        slowRotate.repeatCount = Animation.INFINITE
        slowRotate.interpolator = LinearInterpolator()
        binding.viewLightRays.startAnimation(slowRotate)

        // 5. Pulse Glow Animation
        val pulseScaleX = ObjectAnimator.ofFloat(binding.viewGlow, "scaleX", 1f, 1.2f, 1f)
        val pulseScaleY = ObjectAnimator.ofFloat(binding.viewGlow, "scaleY", 1f, 1.2f, 1f)
        pulseScaleX.repeatCount = ValueAnimator.INFINITE
        pulseScaleY.repeatCount = ValueAnimator.INFINITE
        val pulseSet = AnimatorSet()
        pulseSet.playTogether(pulseScaleX, pulseScaleY)
        pulseSet.duration = 3000
        pulseSet.start()

        // 6. Floating Logo Effect
        val floatAnim = ObjectAnimator.ofFloat(binding.imageViewLogo, "translationY", 0f, -20f, 0f)
        floatAnim.duration = 4000
        floatAnim.repeatCount = ValueAnimator.INFINITE
        floatAnim.start()

        // 7. App Name Entrance
        binding.textViewAppName.animate()
            .alpha(1f)
            .translationY(-20f)
            .setDuration(1000)
            .setStartDelay(800)
            .start()

        // 8. Lens Flare Sweep
        binding.viewLensFlare.alpha = 0.6f
        val flareAnim = ObjectAnimator.ofFloat(binding.viewLensFlare, "translationX", -500f, 500f)
        flareAnim.duration = 2000
        flareAnim.startDelay = 1200
        flareAnim.start()

        // 9. Particle Effect
        startParticles()
    }

    private fun startParticles() {
        val random = Random()
        val handler = Handler(Looper.getMainLooper())
        val particleRunnable = object : Runnable {
            override fun run() {
                if (isFinishing) return
                createParticle(random)
                handler.postDelayed(this, 150)
            }
        }
        handler.post(particleRunnable)
    }

    private fun createParticle(random: Random) {
        val particle = ImageView(this)
        particle.setImageResource(R.drawable.particle)
        val size = random.nextInt(10) + 5
        val params = FrameLayout.LayoutParams(size, size)
        binding.particleContainer.addView(particle, params)

        val startX = random.nextFloat() * binding.root.width
        val startY = binding.root.height.toFloat()

        particle.x = startX
        particle.y = startY
        particle.alpha = 0.8f

        val duration = random.nextInt(2000) + 2000L
        particle.animate()
            .translationY(-200f)
            .alpha(0f)
            .setDuration(duration)
            .withEndAction {
                binding.particleContainer.removeView(particle)
            }
            .start()
    }

    private fun handleNavigation() {
        lifecycleScope.launch {
            delay(3500)
            viewModel.checkNavigation()
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.navigationEvents.collect { event ->
                    when (event) {
                        is SplashViewModel.SplashNavigation.Onboarding -> navigateToLogin()
                        is SplashViewModel.SplashNavigation.Login -> navigateToLogin()
                        is SplashViewModel.SplashNavigation.Main -> navigateToMain()
                    }
                }
            }
        }
    }

    private fun navigateToLogin() {
        startActivity(Intent(this, LoginActivity::class.java))
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }

    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        finish()
    }
}
