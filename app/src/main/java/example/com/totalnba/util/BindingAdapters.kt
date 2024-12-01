package example.com.totalnba.util

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.databinding.BindingAdapter
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import example.com.totalnba.R
import example.com.totalnba.arch.BaseAdapter
import example.com.totalnba.data.base.BaseModel
import example.com.totalnba.ui.view.WinProgressView

object BindingAdapters {
    @JvmStatic
    @BindingAdapter(value = ["isVisibleOrGone"], requireAll = false)
    fun isVisibleOrGone(view: View, isVisible: Boolean?) {
        view.visibility = if (isVisible == true) View.VISIBLE else View.GONE
    }

    @JvmStatic
    @BindingAdapter(value = ["isVisibleOrInvisible"], requireAll = false)
    fun isVisibleOrInvisible(view: View, isVisible: Boolean?) {
        view.visibility = if (isVisible == true) View.VISIBLE else View.INVISIBLE
    }

    @JvmStatic
    @BindingAdapter("setAdapter")
    fun setAdapter(
        recyclerView: RecyclerView,
        adapter: BaseAdapter<ViewDataBinding, BaseModel>?
    ) {
        adapter?.let {
            recyclerView.adapter = it
        }
    }

    @JvmStatic
    @Suppress("UNCHECKED_CAST")
    @BindingAdapter("submitList")
    fun submitList(recyclerView: RecyclerView, list: List<BaseModel>?) {
        val adapter = recyclerView.adapter as BaseAdapter<ViewDataBinding, BaseModel>?
        adapter?.updateData(list ?: listOf())
    }

    @JvmStatic
    @BindingAdapter("setImage")
    fun setImage(view: ImageView, imageName: String) {
        view.setImageResource(imageResolverId(imageName))
    }

    @JvmStatic
    @BindingAdapter("setColor")
    fun setColor(view: ImageView, team: String) {
        view.imageTintList = ColorStateList.valueOf(backgroundResolverByAbbreviation(team))
    }


    @JvmStatic
    @BindingAdapter("setBackground")
    fun setBackground(view: View, backgroundName: String) {
        view.setBackgroundResource(backgroundResolverId(backgroundName))
    }

    @JvmStatic
    @BindingAdapter("setBackgroundAbbr")
    fun setBackgroundAbbr(view: View, backgroundName: String) {
        view.setBackgroundResource(backgroundResolverByAbbreviation(backgroundName))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @JvmStatic
    @BindingAdapter("android:tintMode")
    fun setTintMode(view: View, backgroundName: String) {
        Color.valueOf(backgroundResolverId(backgroundName))
        view.setBackgroundResource(backgroundResolverId(backgroundName))
    }

    @JvmStatic
    @BindingAdapter(value = ["roundFormat"], requireAll = false)
    fun bindRoundValues(view: TextView, double: Double?) {
        if (double == null || double == 0.0) {
            view.text = "-"
        } else {
            view.text = rounding(double).toString()
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["doubleFormat"], requireAll = false)
    fun bindDoubleValues(view: TextView, double: Double?) {
        if (double == null) {
            view.text = "-"
        } else {
            view.text = String.format("%.2f", double)
        }
    }

    @SuppressLint("SetTextI18n")
    @JvmStatic
    @BindingAdapter(value = ["doubleFormatPct"], requireAll = false)
    fun bindDoubleValuesPct(view: TextView, double: Double?) {
        if (double == null) {
            view.text = "-"
        } else {
            view.text = String.format("%.2f", double) + " %"
        }
    }

    @JvmStatic
    @BindingAdapter(value = ["android:text"], requireAll = false)
    fun bindIntegerValues(view: TextView, value: Int?) {
        if (value == null || value == 0) {
            view.text = "-"
        } else {
            view.text = value.toString()
        }
    }

    @JvmStatic
    @BindingAdapter("image_url")
    fun loadImage(imageView: ImageView, url: String) {
        Glide.with(imageView.context)
            .load(url)
            .placeholder(R.drawable.splash_icon)
            .circleCrop()
            .thumbnail()
            .into(imageView)
    }

    @JvmStatic
    @BindingAdapter("setHomeProbability")
    fun setHomeProbability(view: WinProgressView, winHome: Double){
        view.winHomeFloat = winHome.toFloat()
    }

    @JvmStatic
    @BindingAdapter("setAwayProbability")
    fun setAwayProbability(view: WinProgressView, winAway: Double){
        view.winAwayFloat = winAway.toFloat()
    }
}