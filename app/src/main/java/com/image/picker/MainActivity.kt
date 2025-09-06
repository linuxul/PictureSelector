package com.image.picker

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.image.picker.databinding.ActivityMainBinding
import com.luck.picture.lib.animators.BaseAnimationAdapter
import com.luck.picture.lib.config.MediaType
import com.luck.picture.lib.config.SelectionMode
import com.luck.picture.lib.constant.FileSizeUnitConstant
import com.luck.picture.lib.entity.LocalMedia
import com.luck.picture.lib.interfaces.OnAnimationAdapterWrapListener
import com.luck.picture.lib.interfaces.OnPermissionDescriptionListener
import com.luck.picture.lib.interfaces.OnSelectFilterListener
import com.luck.picture.lib.model.PictureSelector
import com.luck.picture.lib.style.StatusBarStyle
import com.luck.picture.lib.style.WindowAnimStyle
import com.luck.picture.lib.utils.DensityUtil.dip2px
import com.luck.picture.lib.utils.ToastUtils
import com.luck.picture.lib.widget.MediumBoldTextView
import com.image.picker.imagepicker.GlideEngine
import com.luck.pictureselector.custom.CustomPreviewExoVideoHolder

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    private lateinit var launcherImageResult: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        launcherImageResult = createActivityImageResultLauncher()

        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        binding.fab.setOnClickListener { view ->
            selectMediaPicker(10)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }


    fun selectMediaPicker(maxCount: Int) {

        val gallery = PictureSelector.create(this@MainActivity)
            .openGallery(MediaType.ALL)

        gallery.setImageSpanCount(3)

        gallery.setMaxSelectNum(
            maxCount,
            0,
            false
        )

        gallery.isNewNumTemplate(true)
        gallery.setStatusBarStyle(buildStatusBar())
        gallery.setWindowAnimStyle(buildWindowAnim())
        gallery.registry(CustomPreviewExoVideoHolder::class.java)
        gallery.setOnAnimationAdapterWrapListener(object :
            OnAnimationAdapterWrapListener {
            override fun wrap(adapter: RecyclerView.Adapter<RecyclerView.ViewHolder>): BaseAnimationAdapter? {
                return when {
//                            rbAlphaListAnim.isChecked -> {
//                                AlphaInAnimationAdapter(adapter)
//                            }
//                            rbScaleListAnim.isChecked -> {
//                                SlideInBottomAnimationAdapter(adapter)
//                            }
                    else -> {
                        null
                    }
                }
            }
        })
        gallery.setLanguage(com.luck.picture.lib.language.Language.KOREA)
        gallery.setPageSize(60)
        gallery.setImageEngine(GlideEngine.create())
        gallery.setMediaConverterEngine(null)
//        gallery.setCropEngine(null)
        gallery.setOnEditorMediaListener(null)
        gallery.setOnFragmentLifecycleListener(null)
        gallery.setOnSelectFilterListener(geSelectFilterListener)
        gallery.setOnPermissionDescriptionListener(getPermissionDescriptionListener)
        gallery.setOnPermissionsApplyListener(null)
        gallery.setSelectionMode(SelectionMode.MULTIPLE)
        gallery.isPreviewZoomEffect(
            true,
            true
        )
        gallery.isGif(true)
        gallery.isWebp(true)
        gallery.isBmp(true)
        gallery.isHeic(true)
        // gallery.setSelectedData(mAdapter.getData())
        gallery.isCameraForegroundService(false)
        // gallery.setOnRecordAudioListener(getRecordAudioListener)
        gallery.isDisplayCamera(false)
        gallery.isFastSlidingSelect(false)
        gallery.isDisplayTimeAxis(true)
        gallery.isEmptyResultBack(false)
        gallery.isOriginalControl(false)
        gallery.isMaxSelectEnabledMask(false)
        gallery.isPreviewImage(true)
        gallery.isPreviewVideo(true)
        gallery.isPreviewAudio(false)
        gallery.isAutoPlay(false)
        gallery.isLoopAutoVideoPlay(false)
        gallery.isVideoPauseResumePlay(false)

        gallery.forResult(launcherImageResult)
    }

    private fun createActivityImageResultLauncher(): ActivityResultLauncher<Intent> {
        return registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val resultCode = result.resultCode
            if (resultCode == RESULT_OK) {
//                val listMedia = PictureSelector.obtainSelectResults(result.data)
//                val imageLists: List<Uri> = listMedia.mapNotNull { media ->
//                    media.path?.let { pathString ->
//                        try {
//                            pathString.toUri()
//                        } catch (e: Exception) {
//                            null
//                        }
//                    }
//                }
//                uploadMedia(uploadMediaType, uploadMediaQuality, imageLists)

            } else if (resultCode == RESULT_CANCELED) {

            }
        }
    }


    private fun buildStatusBar(): StatusBarStyle {
        return StatusBarStyle().apply {
            of(
                false,
                Color.parseColor("#393a3e"),
                Color.parseColor("#393a3e")
            )
        }
    }

    private fun buildWindowAnim(): WindowAnimStyle {
        return WindowAnimStyle().apply {
//            of(R.anim.ps_anim_enter, R.anim.ps_anim_exit)
        }
    }

    private val geSelectFilterListener = object : OnSelectFilterListener {
        override fun onSelectFilter(context: Context, media: LocalMedia): Boolean {
            if (media.size > 10 * FileSizeUnitConstant.MB) {
                ToastUtils.showMsg(context, "파일 최대 크기가 10M 보다 큽니다.")
                return true
            }
            return false
        }
    }


    private val getPermissionDescriptionListener = object : OnPermissionDescriptionListener {
        override fun onDescription(
            fragment: Fragment,
            permissionArray: Array<String>
        ) {
            val viewGroup = fragment.requireView() as ViewGroup
            val dp10 = dip2px(viewGroup.context, 10f)
            val dp15 = dip2px(viewGroup.context, 15f)
            val view = MediumBoldTextView(viewGroup.context)
            view.tag = "TAG_DESCRIPTION_VIEW"
            view.textSize = 14f
            view.setTextColor(Color.parseColor("#333333"))
            view.setPadding(dp10, dp15, dp10, dp15)
            val title: String
            val explain: String
            when {
                TextUtils.equals(
                    permissionArray[0],
                    Manifest.permission.CAMERA
                ) -> {
                    title = "카메라 권한"
                    explain = "사진 업로드 기능을 실행하기 위한 권한이 거부되었습니다. 카메라 및 파일 접근 권한을 허용해주세요."
                }
                TextUtils.equals(
                    permissionArray[0],
                    Manifest.permission.RECORD_AUDIO
                ) -> {
                    title = "오디오 권한"
                    explain = "오디오 업로드 기능을 실행하기 위한 권한이 거부되었습니다. 카메라 및 파일 접근 권한을 허용해주세요."
                }
                else -> {
                    title = "카메라 권한"
                    explain = "사진 업로드 기능을 실행하기 위한 권한이 거부되었습니다. 카메라 및 파일 접근 권한을 허용해주세요."
                }
            }
            val startIndex = 0
            val endOf = startIndex + title.length
            val builder = SpannableStringBuilder(explain)
            builder.setSpan(
                AbsoluteSizeSpan(
                    dip2px(
                        viewGroup.context,
                        16f
                    )
                ), startIndex, endOf, Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
            builder.setSpan(
                ForegroundColorSpan(-0xcccccd),
                startIndex,
                endOf,
                Spannable.SPAN_INCLUSIVE_EXCLUSIVE
            )
            view.text = builder
//            view.background = ContextCompat.getDrawable(
//                viewGroup.context,
//                R.drawable.stt_background
//            )
            val layoutParams = ConstraintLayout.LayoutParams(
                ConstraintLayout.LayoutParams.MATCH_PARENT,
                ConstraintLayout.LayoutParams.WRAP_CONTENT
            )
//            layoutParams.topToBottom = R.id.ps_title_bar
            layoutParams.leftToLeft = ConstraintSet.PARENT_ID
            layoutParams.leftMargin = dp10
            layoutParams.rightMargin = dp10
            viewGroup.addView(view, layoutParams)
        }

        override fun onDismiss(fragment: Fragment) {
            val viewGroup = fragment.requireView() as ViewGroup
            viewGroup.removeView(viewGroup.findViewWithTag("TAG_DESCRIPTION_VIEW"))
        }
    }
}