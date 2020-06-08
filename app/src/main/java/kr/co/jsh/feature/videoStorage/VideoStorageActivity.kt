package kr.co.jsh.feature.videoStorage

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import kr.co.domain.globalconst.Consts
import kr.co.jsh.R
import kr.co.jsh.databinding.ActivityVideoStorageBinding
import kr.co.jsh.feature.videoStorageDetail.VideoDetailActivity
import org.koin.android.ext.android.get

class VideoStorageActivity : AppCompatActivity(), VideoStorageContract.View {
    private lateinit var binding : ActivityVideoStorageBinding
    override lateinit var presenter : VideoStorageContract.Presenter
    private var response = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setupDataBinding()
        initPresenter()
    }

    private fun setupDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_video_storage)
        binding.videoStorage = this@VideoStorageActivity
    }

    private fun initPresenter(){
        presenter = VideoStoragePresenter(this, get(), get(), get(), get())
        response = intent.getIntExtra(Consts.LOGIN_RESPONSE, -1)
//        presenter.loadVideoStorage()

        when(response){
            200 -> {presenter.getServerFileResult()}
            500 -> {
                presenter.loadLocalFileStorageDB()
                presenter.getLocalFileResult()
            }
        }

    }

    override fun setFileResult(list: ArrayList<List<String>>) {
        if(list.isNullOrEmpty()){
            binding.noResultText.visibility = View.VISIBLE
        } else {
            binding.noResultText.visibility = View.GONE
            binding.videoStorageRecycler.apply {
                layoutManager = GridLayoutManager(this@VideoStorageActivity, 2)
                adapter = VideoStorageAdapter(click(), list, context)
            }
        }
        stopAnimation()
    }

    private fun click() = { _: Int, url: String ->
        val intent = Intent(this, VideoDetailActivity::class.java).apply{
            putExtra(Consts.DETAIL_VIDEO, url)
        }
        startActivity(intent)
    }

    override fun startAnimation(){
        binding.loadingAnimation.playAnimation()
        binding.blockingView.visibility = View.VISIBLE
        binding.loadingAnimation.visibility = View.VISIBLE
    }

    override fun stopAnimation(){
        binding.loadingAnimation.cancelAnimation()
        binding.blockingView.visibility = View.GONE
        binding.loadingAnimation.visibility = View.GONE
    }

    fun backBtn(){
        finish()
    }
}