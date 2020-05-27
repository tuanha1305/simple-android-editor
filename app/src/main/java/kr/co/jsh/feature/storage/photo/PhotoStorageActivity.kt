package kr.co.jsh.feature.storage.photo

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import kr.co.domain.globalconst.Consts
import kr.co.domain.globalconst.PidClass
import kr.co.jsh.R
import kr.co.jsh.databinding.ActivityPhotoStorageBinding
import kr.co.jsh.feature.storage.detailPhoto.PhotoDetailActivity
import kr.co.jsh.feature.storage.detailVideo.VideoDetailActivity
import java.net.URL
import org.koin.android.ext.android.get
import java.io.File


class PhotoStorageActivity : AppCompatActivity(), PhotoStorageContract.View {
    lateinit var binding : ActivityPhotoStorageBinding
    lateinit var presenter : PhotoStoragePresenter
    private var response = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupDataBinding()
        initPresenter()
    }
    private fun setupDataBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_photo_storage)
        binding.photoStorage = this@PhotoStorageActivity
    }

    private fun initPresenter(){
        presenter = PhotoStoragePresenter(this, get(), get(), get(), get())
        response = intent.getIntExtra(Consts.LOGIN_RESPONSE, -1)

        when(response){
            200 -> {presenter.getServerImageResult()}
            500 -> {presenter.getLocalImageResult()}
        }
    }

    override fun setImageResult(list: ArrayList<List<String>>) {
        if (list.isNullOrEmpty()) {
            binding.noResultText.visibility = View.VISIBLE
        } else {
            binding.noResultText.visibility = View.GONE
            binding.photoStorageRecycler.apply {
                layoutManager = GridLayoutManager(this@PhotoStorageActivity, 2)
                adapter = PhotoStorageAdapter(click(), list, context)
            }
        }
    }

    private fun click() = { _:Int, url: String ->
        val intent = Intent(this, PhotoDetailActivity::class.java).apply{
            putExtra(Consts.DETAIL_PHOTO, url)
        }
        startActivity(intent)
    }

    fun backButton(){
        finish()
    }
}
